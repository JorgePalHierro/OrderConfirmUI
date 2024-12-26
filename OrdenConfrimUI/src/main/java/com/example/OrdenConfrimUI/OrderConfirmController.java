package com.example.OrdenConfrimUI;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

	List<TransaccionDTO> transaccionesDTO;

	@GetMapping("/")
	public String redireccionar(Model model) {
		model.addAttribute("loginForm", new LoginForm());
		return "login";
	}

	@PostMapping("/ordenesconfirmadas")
	public String login(@ModelAttribute LoginForm loginForm, Model model) {

		if (authService.validate(loginForm.getUsername(), loginForm.getPassword())) {

			Optional<Posusuarios> usuar = userRepository.findById(loginForm.getUsername());

			String NumTienda = usuar.map(Posusuarios::getCNUMTIEN).orElse(null);

			//List<ConfirmacionOrden> transacciones = null;

			/*if (NumTienda.contains("9999")) {
				transacciones = confirmacionOrdenRepository.findAll();
			} else {
				transacciones = confirmacionOrdenRepository.findByTienda(NumTienda);

			}*/

			// Convertir las entidades de ConfirmacionOrden a DTOs (si es necesario)
		//	transaccionesDTO = transacciones.stream().map(this::convertirAArquitecturaDTO).collect(Collectors.toList());

			// Pasar las transacciones filtradas al modelo
			//model.addAttribute("transacciones", transaccionesDTO);

			// List<String> tiendas = Arrays.asList("Tienda 1", "Tienda 2", "Tienda 3");
			List<String> tiendas = cargarListaTiendas(NumTienda);

			model.addAttribute("tiendas", tiendas);
			user_number = loginForm.getUsername();

			return "index";
		} else {
			model.addAttribute("error", "Usuario o contraseña incorrectos");
			return "login";
		}

	}

	@PostMapping("/filtrar")
	public String filtrarDatos(@RequestParam String tienda, @RequestParam String fecha, @RequestParam String estado,
			Model model) throws ParseException {
		SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd");
		// Crear un objeto SimpleDateFormat con el formato de salida
		SimpleDateFormat formatoSalida = new SimpleDateFormat("ddMMyy");

		// Parsear la fecha de entrada
		Date fechaOrden = formatoEntrada.parse(fecha);

		// Formatear la fecha en el nuevo formato
		String fechaFormateada = formatoSalida.format(fechaOrden);

		List<ConfirmacionOrden> transacciones;

		if (estado.contains("MOSTRAR TODO")) {
			System.out.println("Estado:" + estado);
			transacciones = confirmacionOrdenRepository.findByConsecutivoAndTiendaAndFecha("999",tienda.substring(0, 4), fechaFormateada);
		} else {
			System.out.println("Estado:" + estado);
			transacciones = confirmacionOrdenRepository.findByConsecutivoAndTiendaAndFechaAndConfirmacion("999",tienda.substring(0, 4),
					fechaFormateada, tablaConfirmacionDN(estado));
		}

		// Convertir las entidades de ConfirmacionOrden a DTOs (si es necesario)
		transaccionesDTO = transacciones.stream().map(this::convertirAArquitecturaDTO) // Este
																						// método es
																						// un
																						// ejemplo
																						// de
																						// conversión
				.collect(Collectors.toList());

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
				orden.getNumeroOrden(), tablaConversion(orden.getTipo()), orden.getNumTarjeta(),
				convertAndDivide(orden.getImporte()), orden.getVendedor(), orden.getEsquema(),
				orden.getNumAutorizacion(), orden.getCodigoRespuesta(), null,
				tablaConfirmacion(orden.getConfirmacion()));
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


	private String tablaConfirmacionDN(String confirmacion) {
		if (confirmacion.contains("CONFIRMADO"))
			return "1";
		else
			return "0";
	}

	private String tablaConversion(String pos_TENDER_TYPE_CODE) {
		if(pos_TENDER_TYPE_CODE == null)
			pos_TENDER_TYPE_CODE="1";
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

}
