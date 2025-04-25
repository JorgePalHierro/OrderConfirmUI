package com.example.OrdenConfrimUI;

import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ConfirmacionOrdenId implements Serializable {

    public ConfirmacionOrdenId() {
		super();
	}

	public ConfirmacionOrdenId(String tienda, String terminal, String transaccion, String fecha, String numeroOrden) {
		super();
		this.tienda = tienda;
		this.terminal = terminal;
		this.transaccion = transaccion;
		this.fecha = fecha;
		this.numeroOrden = numeroOrden;
	}

	private String tienda;
    private String terminal;
    private String transaccion;
    private String fecha;
    private String numeroOrden;

   

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

    // Método equals() y hashCode() para garantizar que la clave primaria funcione correctamente
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConfirmacionOrdenId that = (ConfirmacionOrdenId) o;

        if (!tienda.equals(that.tienda)) return false;
        if (!terminal.equals(that.terminal)) return false;
        if (!transaccion.equals(that.transaccion)) return false;
        if (!fecha.equals(that.fecha)) return false;
        return numeroOrden.equals(that.numeroOrden);
    }

    @Override
    public int hashCode() {
        int result = tienda.hashCode();
        result = 31 * result + terminal.hashCode();
        result = 31 * result + transaccion.hashCode();
        result = 31 * result + fecha.hashCode();
        result = 31 * result + numeroOrden.hashCode();
        return result;
    }
}
