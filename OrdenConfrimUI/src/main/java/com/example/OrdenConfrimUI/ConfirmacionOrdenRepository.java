package com.example.OrdenConfrimUI;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmacionOrdenRepository extends JpaRepository<ConfirmacionOrden, ConfirmacionOrdenId> {
    List<ConfirmacionOrden> findByTiendaAndFecha(String tienda, String fecha);
    
    List<ConfirmacionOrden> findByConsecutivoAndTienda(long consecutiva, String tienda);
    List<ConfirmacionOrden> findByConsecutivoAndTerminal(long consecutiva, String terminal);
    List<ConfirmacionOrden> findByConsecutivoAndTransaccion(long consecutiva, String transaccion);
    List<ConfirmacionOrden> findByConsecutivoAndConfirmacion(long consecutiva, long confirmacion);

	List<ConfirmacionOrden> findByTienda(String tienda);

	List<ConfirmacionOrden> findByTiendaAndFechaAndConfirmacion(String tienda, String fechaFormateada,
			long confirmacion);

	List<ConfirmacionOrden> findByConsecutivoAndTiendaAndFecha(long consecutiva, String tienda, String fechaFormateada);
	
	List<ConfirmacionOrden> findByConsecutivoAndNumeroOrden(long consecutiva,String numeroOrden);
	
	List<ConfirmacionOrden> findByConsecutivoAndTiendaAndTerminalAndTransaccion (long consecutiva,String tienda, String terminal, String transaccion);

	List<ConfirmacionOrden> findByConsecutivoAndTiendaAndFechaAndConfirmacion(long consecutiva, String tienda,
			String fechaFormateada, long tablaConfirmacionDN);

	 List<ConfirmacionOrden> findByConsecutivoAndTiendaAndTerminalAndTransaccionAndFechaAndNumeroOrden(long consecutiva,
			String numeroTienda, String terminal, String transaccion, String fecha, String orden) ;

	
	
	@Query(value = "SELECT * FROM CONFIRMACION_ORDEN t " +
	        "WHERE t.TIENDA = :tienda " +
			"AND T.CONSECUTIVO = '999'" +
	        "AND TO_DATE(t.FECHA, 'DDMMYY') BETWEEN TO_DATE(:fechaInicio, 'DDMMYY') AND TO_DATE(:fechaFin, 'DDMMYY')",
	        nativeQuery = true)
	List<ConfirmacionOrden> findByTiendaAndFechaInInterval(@Param("tienda") String tienda,
	                                                        @Param("fechaInicio") String fechaInicio,
	                                                        @Param("fechaFin") String fechaFin);

	@Query(value = "SELECT * FROM CONFIRMACION_ORDEN t " +
	        "WHERE t.TIENDA = :tienda " +
			"AND T.CONSECUTIVO = '999'" +
	        "AND TO_DATE(t.FECHA, 'DDMMYY') BETWEEN TO_DATE(:fechaInicio, 'DDMMYY') AND TO_DATE(:fechaFin, 'DDMMYY') " +
	        "AND t.CONFIRMACION = :confirmacion",
	        nativeQuery = true)
	List<ConfirmacionOrden> findByTiendaAndFechaInIntervalAndConfirmacion(@Param("tienda") String tienda,
	                                                                       @Param("fechaInicio") String fechaInicio,
	                                                                       @Param("fechaFin") String fechaFin,
	                                                                       @Param("confirmacion") Long confirmacion);

}
