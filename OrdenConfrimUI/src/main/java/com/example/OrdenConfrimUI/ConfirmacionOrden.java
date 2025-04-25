package com.example.OrdenConfrimUI;

import jakarta.persistence.*; // Para JPA 3.1+ usa jakarta en lugar de javax

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;

@Entity
@Table(name = "CONFIRMACION_ORDEN")
@IdClass(ConfirmacionOrdenId.class)
public class ConfirmacionOrden {

	@Column(name = "CONSECUTIVO")
	private long consecutivo;
	
	@Column(name = "CONFIRMACION")
	private long confirmacion; // NUMBER en DB se mapea como Long o BigDecimal

	@Column(name = "CODIGORESPUESTA", length = 255) // VARCHAR2
	private String codigoRespuesta;

	@Column(name = "NUMAUTORIZACION", length = 255) // VARCHAR2
	private String numAutorizacion;
	@Id
	@Column(name = "FECHA", length = 255) // VARCHAR2
	private String fecha;

	@Column(name = "ESQUEMA", length = 255) // VARCHAR2
	private String esquema;

	@Column(name = "VENDEDOR", length = 255) // VARCHAR2
	private String vendedor;

	@Column(name = "IMPORTE", length = 255) // VARCHAR2
	private long importe;

	@Column(name = "NUMTARJETA", length = 255) // VARCHAR2
	private String numTarjeta;

	@Column(name = "TIPO", length = 255) // VARCHAR2
	private long tipo;
	@Id
	@Column(name = "NUMEROORDEN", length = 255) // VARCHAR2
	private String numeroOrden;
	@Id
	@Column(name = "TRANSACCION", length = 255) // VARCHAR2
	private String transaccion;
	@Id
	@Column(name = "TERMINAL", length = 255) // VARCHAR2
	private String terminal;
	@Id
	@Column(name = "TIENDA", length = 255) // VARCHAR2
	private String tienda;

	@Column(name = "FECHACOMPLETA", length = 255) // VARCHAR2
	private Timestamp fechacompleta;
	
	@Column(name = "STATUS", length = 255) // VARCHAR2
	private String status;

	// Getters y Setters
	public long getConfirmacion() {
		return confirmacion;
	}

	public void setConfirmacion(long confirmacion) {
		this.confirmacion = confirmacion;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getFechaCompleta() {
		return fechacompleta;
	} 

	public void setFechaCompleta(Timestamp timestamp) {
		this.fechacompleta = timestamp;
	}

	public String getCodigoRespuesta() {
		return codigoRespuesta;
	}

	public void setCodigoRespuesta(String codigoRespuesta) {
		this.codigoRespuesta = codigoRespuesta;
	}

	public String getNumAutorizacion() {
		return numAutorizacion;
	}

	public void setNumAutorizacion(String numAutorizacion) {
		this.numAutorizacion = numAutorizacion;
	}

	public String getEsquema() {
		return esquema;
	}

	public void setEsquema(String esquema) {
		this.esquema = esquema;
	}

	public String getVendedor() {
		return vendedor;
	}

	public void setVendedor(String vendedor) {
		this.vendedor = vendedor;
	}

	public long getImporte() {
		return importe;
	}

	public void setImporte(long importe) {
		this.importe = importe;
	}

	public String getNumTarjeta() {
		return numTarjeta;
	}

	public void setNumTarjeta(String numTarjeta) {
		this.numTarjeta = numTarjeta;
	}

	public long getTipo() {
		return tipo;
	}

	public void setTipo(long tipo) {
		this.tipo = tipo;
	}

	public String getNumeroOrden() {
		return numeroOrden;
	}

	public void setNumeroOrden(String numeroOrden) {
		this.numeroOrden = numeroOrden;
	}

	public String getTransaccion() {
		return transaccion;
	}

	public void setTransaccion(String transaccion) {
		this.transaccion = transaccion;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public String getTienda() {
		return tienda;
	}

	public void setTienda(String tienda) {
		this.tienda = tienda;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	
	public long getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(long consecutivo) {
		this.consecutivo = consecutivo;		
	}

	public static ConfirmacionOrden fromDTO(TransaccionDTO dto) {
		ConfirmacionOrden confirmacionOrden = new ConfirmacionOrden();
		confirmacionOrden.setCodigoRespuesta(dto.getCodigoRespuesta());
		confirmacionOrden.setNumAutorizacion(dto.getNumeroAutorizacion());
		confirmacionOrden.setEsquema(dto.getEsquema());
		confirmacionOrden.setVendedor(dto.getVendedor());
		confirmacionOrden.setImporte(Long.valueOf(dto.getImporte()));
		confirmacionOrden.setNumTarjeta(dto.getNumeroTarjeta());
		confirmacionOrden.setTipo(Long.valueOf(dto.getTipo()));
		confirmacionOrden.setNumeroOrden(dto.getNumeroOrden());
		confirmacionOrden.setTransaccion(dto.getTransaccion());
		confirmacionOrden.setTerminal(dto.getTerminal());
		confirmacionOrden.setTienda(dto.getTienda());
		confirmacionOrden.setFecha(dto.getFecha());
		confirmacionOrden.setFechaCompleta(new Timestamp(System.currentTimeMillis()));

		return confirmacionOrden;
	}

	public static String convertirFecha(String fecha) {
		try {
			// Definir el formato original (yyMMdd)
			SimpleDateFormat formatoOriginal = new SimpleDateFormat("yyMMdd");
			// Definir el formato deseado (yyyy-MM-dd)
			SimpleDateFormat formatoDeseado = new SimpleDateFormat("yyyy-MM-dd");

			// Parsear la fecha original a un objeto Date
			java.util.Date fechaDate = formatoOriginal.parse(fecha);

			// Convertir el objeto Date a la cadena con el formato deseado
			return formatoDeseado.format(fechaDate);
		} catch (ParseException e) {
			e.printStackTrace();
			return null; // Retornar null en caso de error
		}
	}

	@Override
	public String toString() {
		return "ConfirmacionOrden [consecutivo=" + consecutivo + ", confirmacion=" + confirmacion + ", codigoRespuesta="
				+ codigoRespuesta + ", numAutorizacion=" + numAutorizacion + ", fecha=" + fecha + ", esquema=" + esquema
				+ ", vendedor=" + vendedor + ", importe=" + importe + ", numTarjeta=" + numTarjeta + ", tipo=" + tipo
				+ ", numeroOrden=" + numeroOrden + ", transaccion=" + transaccion + ", terminal=" + terminal
				+ ", tienda=" + tienda + ", fechacompleta=" + fechacompleta + "]";
	}
}
