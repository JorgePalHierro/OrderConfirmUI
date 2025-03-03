package com.example.OrdenConfrimUI;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmacionOrdenRepository extends JpaRepository<ConfirmacionOrden, ConfirmacionOrdenId> {
    List<ConfirmacionOrden> findByTiendaAndFecha(String tienda, String fecha);
    
    List<ConfirmacionOrden> findByConsecutivoAndTienda(String consecutiva, String tienda);
    List<ConfirmacionOrden> findByConsecutivoAndTerminal(String consecutiva, String terminal);
    List<ConfirmacionOrden> findByConsecutivoAndTransaccion(String consecutiva, String transaccion);
    List<ConfirmacionOrden> findByConsecutivoAndConfirmacion(String consecutiva, String confirmacion);

	List<ConfirmacionOrden> findByTienda(String tienda);

	List<ConfirmacionOrden> findByTiendaAndFechaAndConfirmacion(String tienda, String fechaFormateada,
			String confirmacion);

	List<ConfirmacionOrden> findByConsecutivoAndTiendaAndFecha(String consecutiva, String tienda, String fechaFormateada);
	
	List<ConfirmacionOrden> findByConsecutivoAndNumeroOrden(String consecutiva,String numeroOrden);
	
	List<ConfirmacionOrden> findByConsecutivoAndTiendaAndTerminalAndTransaccion (String consecutiva,String tienda, String terminal, String transaccion);

	List<ConfirmacionOrden> findByConsecutivoAndTiendaAndFechaAndConfirmacion(String string, String substring,
			String fechaFormateada, String tablaConfirmacionDN);
}
