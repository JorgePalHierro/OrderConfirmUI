package com.example.OrdenConfrimUI;

import java.time.LocalDate;

public class Ticket {
	private String Orden;
    private String terminal;
    private String transaccion;
    private String correo;
    private String cuerpo;
    private String numeroTienda;
    private String fecha;
    private String fechaFormateada;
    private String monedas;
    private String total;
    private String confirmacion;
    private String instrumento;
    

    // Constructor
    public Ticket() {}

    // Getters y Setters
    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String ticket) {
        this.terminal = ticket;
    }

    public String getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(String identificador) {
        this.transaccion = identificador;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public String getNumeroTienda() {
        return numeroTienda;
    }

    public void setNumeroTienda(String numeroTienda) {
        this.numeroTienda = numeroTienda;
    }

   

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getFechaFormateada() {
        return fechaFormateada;
    }

    public void setFechaFormateada(String fechaFormateada) {
        this.fechaFormateada = fechaFormateada;
    }

	public String getOrden() {
		return Orden;
	}

	public void setOrden(String orden) {
		Orden = orden;
	}
	
	public String getMonedas() {
		return monedas;
	}

	public void setMonedas(String monedas) {
		this.monedas = monedas;
	}
	
	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}
	
	public String getConfirmacion() {
		return confirmacion;
	}

	public void setConfimracion(String confirmacion) {
		this.confirmacion = confirmacion;
	}
	
	public String getInstrumento() {
		return instrumento;
	}

	public void setInstrumento(String instrumento) {
		this.instrumento = instrumento;
	}
}
