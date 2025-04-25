package com.example.OrdenConfrimUI;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Services.ConsultaService;
import com.example.Utils.ConfirmacionOrdenService;
import com.example.Utils.Log;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller

public class OrderConfirmController {
	String user_number;
	@Autowired
	private AuthService authService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TiendasRepository tiendasRepository;
	@Autowired
	private ConfirmacionOrdenRepository confirmacionOrdenRepository;
	@Autowired
	private Log log = new Log();
	

	private PatchService patchService;
	 private final ConfirmacionOrdenService confirmacionOrdenService;

	 @Autowired
	    public OrderConfirmController(ConfirmacionOrdenService confirmacionOrdenService, PatchService patchService) {
	        this.confirmacionOrdenService = confirmacionOrdenService;
	        this.patchService = patchService;
	    }
	

	List<TransaccionDTO> transaccionesDTO;
	
	@GetMapping("/")
	public String redireccionar(Model model) {
		model.addAttribute("loginForm", new LoginForm());
		return "login";
	}

	@PostMapping("/ordenesconfirmadas")
	public String login(@ModelAttribute LoginForm loginForm, Model model) {
		log.escribirLog("Intento de login con usuario: " + loginForm.getUsername());
		if (authService.validate(loginForm.getUsername(), loginForm.getPassword())) {

			log.escribirLog("Logeo Exitoso");
			Optional<Posusuarios> usuar = userRepository.findById(loginForm.getUsername());

			String NumTienda = usuar.map(Posusuarios::getCNUMTIEN).orElse(null);

			List<String> tiendas = cargarListaTiendas(NumTienda);

			model.addAttribute("tiendas", tiendas);
			user_number = loginForm.getUsername();

			return "index";
		} else {
			log.escribirLog("Error de login: Usuario o contraseña incorrectos");
			model.addAttribute("error", "Usuario o contraseña incorrectos");
			return "login";
		}

	}

	@PostMapping("/filtrar")
	public String filtrarDatos(@RequestParam String tienda,
	                            @RequestParam String fechaInicio,
	                            @RequestParam String fechaFin,
	                            @RequestParam String estado,
	                            Model model) throws ParseException {

	    log.escribirLog("Filtro de datos: tienda=" + tienda + ", fechaInicio=" + fechaInicio + ", fechaFin=" + fechaFin + ", estado=" + estado);

	    // Formatos para parsear y formatear
	    SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd");
	    SimpleDateFormat formatoSalida = new SimpleDateFormat("ddMMyy");

	    // Convertir fechas
	    Date fechaInicioDate = formatoEntrada.parse(fechaInicio);
	    Date fechaFinDate = formatoEntrada.parse(fechaFin);
	    String fechaInicioFormateada = formatoSalida.format(fechaInicioDate);
	    String fechaFinFormateada = formatoSalida.format(fechaFinDate);

	    List<ConfirmacionOrden> transacciones;

	    if (estado.contains("MOSTRAR TODO")) {
	        transacciones = confirmacionOrdenRepository.findByTiendaAndFechaInInterval(
	                tienda.substring(0, 4), fechaInicioFormateada, fechaFinFormateada);
	    } else {
	        transacciones = confirmacionOrdenRepository.findByTiendaAndFechaInIntervalAndConfirmacion(
	                tienda.substring(0, 4), fechaInicioFormateada, fechaFinFormateada,
	                Long.parseLong(tablaConfirmacionDN(estado)));
	    }

	    transaccionesDTO = transacciones.stream().map(this::convertirAArquitecturaDTO).collect(Collectors.toList());

	    model.addAttribute("transacciones", transaccionesDTO);
	    model.addAttribute("tiendas", cargarListaTiendas("9999"));

	    return "index"; // Regresar la vista
	}


	@PostMapping("/filtrarConfirm")
	public String filtrarDatosConfirmacion(@RequestParam String tienda, @RequestParam String terminal,
			@RequestParam String transaccion, @RequestParam String estado, Model model) throws ParseException {

		System.out.println("Tienda:" + tienda + "  Terminal: " + terminal);
		String tiendaFiltrada = tienda.substring(0, 4);
		String consecutivo = "999"; // Valor predeterminado

		List<ConfirmacionOrden> transaccionesTienda = null;
		List<ConfirmacionOrden> transaccionesTerminal = null;
		List<ConfirmacionOrden> transaccionesTransaccion = null;
		List<ConfirmacionOrden> transaccionesEstado = null;
		List<ConfirmacionOrden> resultados;
		System.out.println("Tienda: " + tienda);

		System.out.println("Terminal: " + terminal);

		System.out.println("Transaccion: " + transaccion);

		System.out.println("Estado: " + estado);

		transaccionesTienda = confirmacionOrdenRepository.findByConsecutivoAndTienda(Long.parseLong(consecutivo),
				tiendaFiltrada);
		if (!terminal.isEmpty())
			transaccionesTerminal = confirmacionOrdenRepository
					.findByConsecutivoAndTerminal(Long.parseLong(consecutivo), terminal);
		if (!transaccion.isEmpty())
			transaccionesTransaccion = confirmacionOrdenRepository
					.findByConsecutivoAndTransaccion(Long.parseLong(consecutivo), transaccion);
		if (!estado.contains("MOSTRAR TODO"))
			transaccionesEstado = confirmacionOrdenRepository.findByConsecutivoAndConfirmacion(
					Long.parseLong(consecutivo), Long.parseLong(tablaConfirmacionDN(estado)));

		resultados = obtenerRegistrosComunes(transaccionesTienda, transaccionesTerminal, transaccionesTransaccion,
				transaccionesEstado);

		// transacciones =
		// confirmacionOrdenRepository.findByConsecutivoAndTiendaAndTerminalAndTransaccionAndEstado("999",
		// tienda.substring(0, 4), terminal, transaccion,tablaConfirmacionDN(estado));

		// Convertir las entidades de ConfirmacionOrden a DTOs (si es necesario)
		transaccionesDTO = resultados.stream().map(this::convertirAArquitecturaDTO).collect(Collectors.toList());

		// Pasar las transacciones filtradas al modelo
		model.addAttribute("transacciones", transaccionesDTO);

		// Pasar las tiendas al modelo para el formulario de filtro
		model.addAttribute("tiendas", cargarListaTiendas("9999"));

		return "index"; // Recargar la vista con los resultados
	}

	@PostMapping("/filtrarOrden")
	public String filtrarDatosOrden(@RequestParam String orden, Model model) throws ParseException {

		List<ConfirmacionOrden> transacciones;

		transacciones = confirmacionOrdenRepository.findByConsecutivoAndNumeroOrden(Long.parseLong("999"), orden);

		// Convertir las entidades de ConfirmacionOrden a DTOs (si es necesario)
		transaccionesDTO = transacciones.stream().map(this::convertirAArquitecturaDTO).collect(Collectors.toList());

		// Pasar las transacciones filtradas al modelo
		model.addAttribute("transacciones", transaccionesDTO);

		// Pasar las tiendas al modelo para el formulario de filtro
		model.addAttribute("tiendas", cargarListaTiendas("9999"));

		return "index"; // Recargar la vista con los resultados
	}

	@PostMapping("/descargarExcel")
	public ResponseEntity<byte[]> descargarExcel() {
		System.out.println("tamaño de trsanccion: " + transaccionesDTO.size());
		if (transaccionesDTO.size() > 0) {
			try (Workbook workbook = new XSSFWorkbook()) {
				// Crear una hoja
				Sheet sheet = workbook.createSheet("Transacciones");

				// Crear fila de encabezados
				Row header = sheet.createRow(0);
				String[] headers = { "Tienda", "Terminal", "Transacción", "Fecha", "Orden", "Tipo", "Vendedor",
						"Importe", "Estado" };
				for (int i = 0; i < headers.length; i++) {
					Cell cell = header.createCell(i);
					cell.setCellValue(headers[i]);
					cell.setCellStyle(createHeaderCellStyle(workbook)); // Método para estilo de encabezado
				}

				// Agregar los datos de las transacciones DTO
				int rowIndex = 1; // Comenzamos desde la fila 1 para los datos
				for (TransaccionDTO transaccion : transaccionesDTO) {
					Row row = sheet.createRow(rowIndex++);
					row.createCell(0).setCellValue(transaccion.getTienda());
					row.createCell(1).setCellValue(transaccion.getTerminal());
					row.createCell(2).setCellValue(transaccion.getTransaccion());
					row.createCell(3).setCellValue(transaccion.getFecha());
					row.createCell(4).setCellValue(transaccion.getNumeroOrden());
					row.createCell(5).setCellValue(transaccion.getTipo());
					row.createCell(6).setCellValue(transaccion.getVendedor());
					row.createCell(7).setCellValue(transaccion.getImporte());
					row.createCell(8).setCellValue(transaccion.getConfirmacion());
				}

				// Ajustar las columnas para que se adapten al contenido
				for (int i = 0; i < headers.length; i++) {
					sheet.autoSizeColumn(i);
				}

				// Convertir el workbook a un array de bytes
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				workbook.write(outputStream);

				// Retornar el archivo como respuesta
				return ResponseEntity.ok()
						.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=transacciones.xlsx")
						.body(outputStream.toByteArray());
			} catch (IOException e) {
				return ResponseEntity.internalServerError().build();
			}
		}
		return null;
	}

	@PostMapping("/confirmar")
	public ResponseEntity<String> confirmar(@RequestBody Ticket confirmData) {
		log.escribirLog("Intentando Confirmar Registro : tienda=" + confirmData.getNumeroTienda() + ", terminal="
				+ confirmData.getTerminal() + ",transaccion=" + confirmData.getTransaccion() + ", estado="
				+ confirmData.getFecha());

		// Mostrar en consola los datos recibidos (puedes quitarlo en producción)
		System.out.println("Tienda: " + confirmData.getNumeroTienda());
		System.out.println("Terminal: " + confirmData.getTerminal());
		System.out.println("Transacción: " + confirmData.getTransaccion());
		System.out.println("Fecha: " + confirmData.getFecha());
		System.out.println("Número de Orden: " + confirmData.getOrden());
		System.out.println("Vendedor: " + confirmData.getCorreo());
		System.out.println("Importe: " + confirmData.getTotal());
		System.out.println("Confirmación: " + confirmData.getConfirmacion());

		// Aquí puedes llamar a tus servicios (por ejemplo, ConsultaService,
		// patchService, etc.)
		ConsultaService consulta = new ConsultaService();
		System.out.println(consulta.getApiResponse(confirmData.getOrden()));
		try {
			System.out
					.println("Instrumento: " + getPaymentInstrumentId(consulta.getApiResponse(confirmData.getOrden())));
			confirmData.setInstrumento(getPaymentInstrumentId(consulta.getApiResponse(confirmData.getOrden())));
		} catch (IOException e) {
			e.printStackTrace();
		}

		String resp;
		try {
			// Ejemplo de llamada al servicio PATCH (ajusta los parámetros según
			// corresponda)
			resp = patchService.sendPatchRequest(confirmData.getOrden(), confirmData.getInstrumento(),
					confirmData.getTerminal(), confirmData.getNumeroTienda(), confirmData.getTransaccion(),
					confirmData.getFecha());
			System.out.println("Respuesta de clase patch: " + resp);

			if (resp.contains("Error: Registro duplicado")) {
				resp = "Error: Registro duplicado";
				log.escribirLog("Error: Registro duplicado");

			} else if (resp.contains("Registro ya confirmado")) {
				resp = "Registro ya confirmado";
				log.escribirLog("Registro ya confirmado");
			} else {
				if (resp.contains("Registro caducado")) {
					resp = "Error: Registro vencido";
					log.escribirLog("Error: Registro vencido");
				} else {
					resp = "Registro confirmado con exito, espere unos minutos para que se actualice el registro.";
					log.escribirLog(
							"Registro confirmado con exito, espere unos minutos para que se actualice el registro.");
					
					

					List<ConfirmacionOrden> ordenList = confirmacionOrdenRepository
							.findByConsecutivoAndTiendaAndTerminalAndTransaccion(Long.parseLong("999"),
									confirmData.getNumeroTienda(), confirmData.getTerminal(),
									confirmData.getTransaccion());

					System.out.println("Fecha:" + confirmData.getFecha() + " Orden:" + confirmData.getOrden()
							+ " Transacción:" + confirmData.getTransaccion() + " Terminal:" + confirmData.getTerminal()
							+ " Tienda:" + confirmData.getNumeroTienda());

					if (!ordenList.isEmpty()) {
						ConfirmacionOrden orden = ordenList.get(0); // Tomar el primer elemento de la lista
						System.out.println("Orden 1:" +orden.toString());
						confirmacionOrdenService.insertOrUpdateRecords(orden);
						//orden.setConfirmacion(1); // Actualizar el campo CONFIRMACION
						//confirmacionOrdenRepository.save(orden); // Guardar la orden actualizada
						return ResponseEntity.ok("Confirmación actualizada correctamente.");
					} else {
						return ResponseEntity.badRequest()
								.body("Orden no encontrada con los parámetros proporcionados.");
					}
				}
			}
		} catch (IOException e) {
			resp = "Error en el servidor:  " + e.getMessage();
			log.escribirLog("Error en el servidor:  " + e.getMessage());

		}

		// Devuelve la respuesta apropiada (puede ser JSON, texto, etc.)
		return ResponseEntity.ok(resp);
	}

	// Método auxiliar para definir estilo de encabezado
	private CellStyle createHeaderCellStyle(Workbook workbook) {
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setBold(true);
		style.setFont(font);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		return style;
	}

	private TransaccionDTO convertirAArquitecturaDTO(ConfirmacionOrden orden) {
		return new TransaccionDTO(orden.getTienda(), orden.getTerminal(), orden.getTransaccion(), orden.getFecha(),
				orden.getNumeroOrden(), String.valueOf(orden.getTipo()), orden.getNumTarjeta(),
				convertAndDivide(String.valueOf(orden.getImporte())), orden.getVendedor(), orden.getEsquema(),
				orden.getNumAutorizacion(), orden.getCodigoRespuesta(), null,
				tablaConfirmacion(String.valueOf(orden.getConfirmacion())));
	}

	// Método para convertir un String, dividir entre 100 y devolver el resultado
	// como String
	public String convertAndDivide(String input) {
		try {
			// Convertir el String a double
			double number = Double.parseDouble(input);

			// Dividir el número entre 100
			double result = number / 100;

			// Convertir el resultado a String y devolverlo
			return Double.toString(result);
		} catch (NumberFormatException e) {
			// Manejo de excepciones si la conversión falla
			return "Error: Input no válido";
		}
	}

	private List<String> cargarListaTiendas(String numTienda) {
		List<String> descripciones;
		if (numTienda.contains("999")) {
			descripciones = tiendasRepository.findAll().stream()
					.map(tienda -> tienda.getCNUMTIEN() + " - " + tienda.getDDESCRIP()).sorted()
					.collect(Collectors.toList());
		} else {
			descripciones = tiendasRepository.findAll().stream().filter(t -> t.getCNUMTIEN().equals(numTienda)) // Filtrar
																												// por
																												// el
																												// número
																												// de
																												// tienda
					.map(t -> t.getCNUMTIEN() + " - " + t.getDDESCRIP()) // Transformar en formato deseado
					.sorted() // Ordenar
					.collect(Collectors.toList()); // Convertir a lista
		}

		return descripciones;
	}

	private String tablaConfirmacion(String confirmacion) {
		if (confirmacion.equals("1")) {
			return "CONFIRMADO";
		} else if (confirmacion.equals("0")) {
			return "SIN CONFIRMAR";
		} else if (confirmacion.equals("2")) {
			return "ORDEN NO ENCONTRADA";
		} else {
			return "ESTADO DESCONOCIDO";
		}
	}

	public static String getPaymentInstrumentId(String jsonResponse) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode rootNode = objectMapper.readTree(jsonResponse);

		// Acceder a la lista de payment_instruments
		JsonNode paymentInstruments = rootNode.path("payment_instruments");

		if (paymentInstruments.isArray() && paymentInstruments.size() > 0) {
			return paymentInstruments.get(0).path("payment_instrument_id").asText();
		}

		return null; // Retornar null si no se encuentra el ID
	}

	private String tablaConfirmacionDN(String confirmacion) {
		if (confirmacion.contains("CONFIRMADO"))
			return "1";
		else
			return "0";
	}

	private String tablaConversion(String pos_TENDER_TYPE_CODE) {
		if (pos_TENDER_TYPE_CODE == null)
			pos_TENDER_TYPE_CODE = "1";
		switch (pos_TENDER_TYPE_CODE) {
		case "1":
			return "Efectivo";
		case "8":
			return "TCPH";
		case "4":
		case "5":
			return "Bcos";
		case "3":
			return "AMEX";
		case "10":
			return "CUPH";
		case "23":
			return "Vales";
		case "2":
			return "Cheque";
		default:
			return "UNKNOWN_CODE";
		}
	}

	public static List<ConfirmacionOrden> obtenerRegistrosComunes(List<ConfirmacionOrden> transaccionesTienda,
			List<ConfirmacionOrden> transaccionesTerminal, List<ConfirmacionOrden> transaccionesTransaccion,
			List<ConfirmacionOrden> transaccionesEstado) {

		// Filtrar listas no nulas
		List<List<ConfirmacionOrden>> listasNoNulas = Arrays
				.asList(transaccionesTienda, transaccionesTerminal, transaccionesTransaccion, transaccionesEstado)
				.stream().filter(Objects::nonNull).collect(Collectors.toList());

		if (listasNoNulas.isEmpty()) {
			return Collections.emptyList();
		}

		// Convertir la primera lista en un Set para comparar
		Set<ConfirmacionOrden> interseccion = new HashSet<>(listasNoNulas.get(0));

		// Intersectar con las demás listas
		for (int i = 1; i < listasNoNulas.size(); i++) {
			interseccion.retainAll(new HashSet<>(listasNoNulas.get(i)));
		}

		return new ArrayList<>(interseccion);
	}
	
	 
	

}
