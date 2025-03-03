package com.example.OrdenConfrimUI;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class PatchService {

	@Autowired
	private ConfirmacionOrdenRepository confirmacionOrdenRepository;

	private final OkHttpClient okHttpClient;

	// Spring Boot inyectará automáticamente el OkHttpClient aquí
	public PatchService(OkHttpClient okHttpClient) {
		this.okHttpClient = okHttpClient;
	}

	public void someMethod() {
		// Ejemplo de uso de OkHttpClient
		System.out.println("OkHttpClient inyectado correctamente: " + okHttpClient);
	}

	public String sendPatchRequest(String numero_orden, String instrumento, String terminal, String tienda,
			String transaccion, String fecha) throws IOException {
		String url = "https://ph-bypass-pos-exp-api.us-w1.cloudhub.io/api/v1/orders/" + numero_orden
				+ "/payment-instruments/" + instrumento;

		List<ConfirmacionOrden> transacciones;

		//Optional<OnlinePosHeader> allPosHeader = posHeaderService.findByParams(tienda, terminal, transaccion, fecha);
		//Optional<OnlinePosPagos> allPosPagos = posPagosService.findByParams(tienda, terminal, transaccion, fecha);

		transacciones = confirmacionOrdenRepository.findByConsecutivoAndNumeroOrden("999", numero_orden);

		String ticket = tienda + terminal + transaccion + fecha;
		ticket = numeroVerificador(ticket);

		if (transacciones.size() == 1) {

			System.out.println("Lista de registros");

			for (ConfirmacionOrden registro : transacciones) {
				System.out.println(registro.toString());

				//OnlinePosPagos pagos = allPosPagos.get();
				//OnlinePosHeader header = allPosHeader.get();
				registro.setTipo(tablaConversion(registro.getTipo()));

				// JSON request body
				double amounTender = Double.parseDouble(registro.getImporte()) / 100;
				System.out.println("Importe: " + amounTender);

				// JSON request body
				String json = "{\n" + "  \"amount\": " + amounTender + ",\n"
						+ "  \"payment_method_id\": \"PAY_IN_STORE\",\n" + "  \"c_paymentType\": \"" 
						+ registro.getTipo() + "\",\n" + "  \"payment_card\": {\n"
						+ "    \"card_type\": \"" + registro.getTipo() + "\",\n" + "    \"number\": \"" 
						+ registro.getNumTarjeta() + "\",\n" + "    \"holder\": \"VENTA ASISTIDA\"\n" + "  },\n"
						+ "  \"c_AS_palacioStoreID\": \"" + tienda + "\",\n" + "  \"c_AS_employeeId\": \"" 
						+ registro.getVendedor() + "\",\n" + "  \"c_AS_ticketNumber\": \"" + ticket + "\",\n"
						+ "  \"c_AS_monedasBarcode\": \"000000000000000000000\",\n" + "  \"c_AS_palacioPoints\": 0,\n"
						+ "  \"c_AS_monedas\": 0,\n"
						+ "  \"c_AS_detailedPaymentInformation\": \"[{\\\"schemaId\\\":\\\""
						+ registro.getEsquema() + "\\\",\\\"transactionDateTime\\\":\\\""
						+ obtenerFechaHoraActual() + "\\\",\\\"externalTransactionCode\\\":\\\""
						+ registro.getTransaccion()
						+ "\\\",\\\"transactionID\\\":\\\"4008306119\\\",\\\"originalTerminalId\\\":\\\"\\\",\\\"originalTransactionID\\\":\\\"\\\",\\\"transactionInfoCount\\\":\\\""
						+ registro.getTransaccion() + "\\\",\\\"terminal\\\":\\\"" + terminal
						+ "\\\",\\\"approval\\\":\\\"" + registro.getNumAutorizacion()
						+ "\\\",\\\"responseCode\\\":\\\"" + registro.getCodigoRespuesta()
						+ "\\\",\\\"responseDisplay\\\":\\\"TRANSACCION APROBADA\\\"}]\"\n" + "}";

				System.out.println("Peticion 1:" + json);

				RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));

				Request request = new Request.Builder().url(url).patch(body)
						.addHeader("client_id", "87cd348c017447b9991fc1eb4e0cccd4")
						.addHeader("client_secret", "de22aa5e10514445Ac9eDa16475E5bFf").build();

				try (Response response = okHttpClient.newCall(request).execute()) {
					// Lee el cuerpo de la respuesta solo una vez
					String responseBody = response.body().string();
					
					// Muestra el contenido de la respuesta
					System.out.println("Respuesta:" + responseBody);

					if (response.isSuccessful()) {
						return responseBody; // Devuelve el cuerpo de la respuesta
					} else {
						if (responseBody.contains("status is 'FAILED'")) {
							return "Registro caducado";
						} else if (responseBody.contains("for order status CREATED, but status is 'NEW'"))
							return "Registro ya confirmado";
						throw new IOException("Failed : HTTP error code : " + response.code() + ", reason: "
								+ response.message() + ", details: " + responseBody);
					}
				}
			}

		} else {
			return "Error: Registro duplicado";
		}
		return "Registro no encontrado";
	}


	private String tablaConversion(String pos_TENDER_TYPE_CODE) {
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

	public static String numeroVerificador(String ticket) {

		int residuo;

		String todo = ticket;
		ArrayList numeros = new ArrayList();

		char[] valores = todo.toCharArray();

		for (int i = 0; i < valores.length; i = i + 2) {
			int a = (Character.getNumericValue(valores[i])) * 3;

			numeros.add(a);
		}

		for (int i = 1; i < valores.length; i = i + 2) {
			int b = Character.getNumericValue(valores[i]);

			numeros.add(b);
		}
		int suma = 0;
		for (int i = 0; i < numeros.size(); i++) {

			int num = (int) numeros.get(i);
			suma = suma + num;
		}

		residuo = suma % 10;
		if (residuo != 0) {
			residuo = 10 - residuo;
		}

		ticket = todo + residuo;
		return ticket;
	}

	public static String obtenerFechaHoraActual() {
		// Obtener la fecha y hora actual
		LocalDateTime fechaHoraActual = LocalDateTime.now();

		// Formatear la fecha y hora en el formato requerido
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		return fechaHoraActual.format(formato);
	}

}
