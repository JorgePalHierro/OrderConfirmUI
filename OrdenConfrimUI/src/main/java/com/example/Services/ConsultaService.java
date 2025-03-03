package com.example.Services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@Service
public class ConsultaService {

	private static final String URL = "https://ph-bypass-pos-exp-api.us-w1.cloudhub.io/api/v1/orders/";
	private static final String CLIENT_ID = "87cd348c017447b9991fc1eb4e0cccd4";
	private static final String CLIENT_SECRET = "de22aa5e10514445Ac9eDa16475E5bFf";

	public String getApiResponse(String numOrden) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("client_id", CLIENT_ID);
		headers.set("client_secret", CLIENT_SECRET);

		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = null;
		try {
			response = restTemplate.exchange(URL + numOrden, HttpMethod.GET, entity, String.class);

		} catch (HttpClientErrorException e) {
			// Maneja errores del cliente (4xx)
			if (e.getStatusCode().value() == 404) {
				// Procesar el error 404 específicamente
				System.err.println("Error 404: Recurso no encontrado - " + e.getResponseBodyAsString());
				// Aquí puedes lanzar una excepción personalizada o retornar un valor
				// predeterminado
				return "No se encontró la orden";
			} else {
				// Manejar otros errores de cliente
				System.err.println("Error de cliente: " + e.getStatusCode() + " - " + e.getMessage());
				return "Error al consultar la API: " + e.getMessage();
			}
		} catch (HttpServerErrorException e) {
			// Maneja errores del servidor (5xx)
			return "Error al consultar la API: " + e.getMessage();
			// System.err.println("Error de servidor: " + e.getStatusCode() + " - " +
			// e.getMessage());
			// throw new RuntimeException("Error del servidor: " + e.getMessage(), e);
		} catch (Exception e) {
			return "Error al consultar la API: " + e.getMessage();
			// Maneja cualquier otra excepción
			// System.err.println("Error general: " + e.getMessage());
			// throw new RuntimeException("Error inesperado: " + e.getMessage(), e);

		}

		return response.getBody();
	}

	// Excepción personalizada para el caso de no encontrar la orden
	public static class OrderNotFoundException extends RuntimeException {
		public OrderNotFoundException(String message) {
			super(message);
		}

	}

}