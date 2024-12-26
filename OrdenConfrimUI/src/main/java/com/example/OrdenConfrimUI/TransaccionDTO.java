package com.example.OrdenConfrimUI;

public class TransaccionDTO {
    public TransaccionDTO(String tienda, String terminal, String transaccion, String fecha, String numeroOrden,
			String tipo, String numeroTarjeta, String importe, String vendedor, String esquema,
			String numeroAutorizacion, String codigoRespuesta, String instrumento, String confirmacion) {
		super();
		this.tienda = tienda;
		this.terminal = terminal;
		this.transaccion = transaccion;
		this.fecha = fecha;
		this.numeroOrden = numeroOrden;
		this.tipo = tipo;
		this.numeroTarjeta = numeroTarjeta;
		this.importe = importe;
		this.vendedor = vendedor;
		this.esquema = esquema;
		this.numeroAutorizacion = numeroAutorizacion;
		this.codigoRespuesta = codigoRespuesta;
		this.instrumento = instrumento;
		this.confirmacion = confirmacion;
	}

	private String tienda;
    private String terminal;
    private String transaccion;
    private String fecha;
    private String numeroOrden;
    private String tipo;
    private String numeroTarjeta;
    private String importe;
    private String vendedor;
    private String esquema;
    private String numeroAutorizacion;
    private String codigoRespuesta;
    private String instrumento;
    private String confirmacion;
    

	

	// Getters y setters
    public String getTienda() {
        return tienda;
    }

    public void setTienda(String tienda) {
        this.tienda = tienda;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(String transaccion) {
        this.transaccion = transaccion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getNumeroOrden() {
        return numeroOrden;
    }

    public void setNumeroOrden(String numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public String getImporte() {
        return importe;
    }

    public void setImporte(String importe) {
        this.importe = importe;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public String getEsquema() {
        return esquema;
    }

    public void setEsquema(String esquema) {
        this.esquema = esquema;
    }

    public String getNumeroAutorizacion() {
        return numeroAutorizacion;
    }

    public void setNumeroAutorizacion(String numeroAutorizacion) {
        this.numeroAutorizacion = numeroAutorizacion;
    }

    public String getCodigoRespuesta() {
        return codigoRespuesta;
    }

    public void setCodigoRespuesta(String codigoRespuesta) {
        this.codigoRespuesta = codigoRespuesta;
    }
    
    public String getInstrumento() {
        return instrumento ;
    }

    public void setInstrumento(String instrumento) {
        this.instrumento = instrumento;
    }
    
    public String getConfirmacion() {
        return confirmacion ;
    }

    public void setConfirmacion(String confirmacion) {
        this.confirmacion = confirmacion;
    }
    @Override
    public String toString() {
        return "TransaccionDTO{" +
                "tienda='" + tienda + '\'' +
                ", terminal='" + terminal + '\'' +
                ", transaccion='" + transaccion + '\'' +
                ", fecha='" + fecha + '\'' +
                ", numeroOrden='" + numeroOrden + '\'' +
                ", tipo='" + tipo + '\'' +
                ", numeroTarjeta='" + numeroTarjeta + '\'' +
                ", importe='" + importe + '\'' +
                ", vendedor='" + vendedor + '\'' +
                ", esquema='" + esquema + '\'' +
                ", numeroAutorizacion='" + numeroAutorizacion + '\'' +
                ", codigoRespuesta='" + codigoRespuesta + '\'' +
                ", instrumento='" + instrumento + '\'' +
                '}';
    }
}
