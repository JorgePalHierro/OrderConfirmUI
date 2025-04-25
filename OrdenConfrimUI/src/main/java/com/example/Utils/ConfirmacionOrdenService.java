package com.example.Utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.OrdenConfrimUI.ConfirmacionOrden;
import com.example.OrdenConfrimUI.ConfirmacionOrdenRepository;


import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class ConfirmacionOrdenService {

    @PersistenceContext
    private EntityManager entityManager;

    
    public static void insertOrUpdateRecords(ConfirmacionOrden item) {
        Properties properties = new Properties();
        String url = "jdbc:oracle:thin:@10.10.13.14:1529:sfc";
        String username = "MACBHE";
        String password = "D3nV5Er9";

        // Conectar a la base de datos
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Conexión exitosa a la base de datos");

            // Iniciar transacción
            connection.setAutoCommit(false);

            // Definir la sentencia SQL para insertar o actualizar los registros
            String sql = "MERGE INTO CONFIRMACION_ORDEN t " +
                    "USING dual " +
                    "ON (t.CONSECUTIVO = ? AND t.TIENDA = ? AND t.TERMINAL = ? AND " +
                    "    t.TRANSACCION = ? AND t.FECHA = ? AND t.NUMEROORDEN = ?) " +
                    "WHEN MATCHED THEN " +
                    "UPDATE SET t.FECHACOMPLETA = ?, t.TIPO = ?, t.NUMTARJETA = ?, " +
                    "           t.IMPORTE = ?, t.VENDEDOR = ?, t.ESQUEMA = ?, " +
                    "           t.NUMAUTORIZACION = ?, t.CODIGORESPUESTA = ?, t.CONFIRMACION = ?,t.STATUS = ? " +
                    "WHEN NOT MATCHED THEN " +
                    "INSERT (CONSECUTIVO, TIENDA, TERMINAL, TRANSACCION, FECHA, NUMEROORDEN, " +
                    "        FECHACOMPLETA, TIPO, NUMTARJETA, IMPORTE, VENDEDOR, ESQUEMA, " +
                    "        NUMAUTORIZACION, CODIGORESPUESTA, CONFIRMACION,STATUS ) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                // Iterar sobre cada orden y ejecutar la inserción/actualización
                System.out.println("Registro modificado: " + item.toString());
                // Parámetros para la condición ON
                stmt.setLong(1, item.getConsecutivo());
                stmt.setString(2, item.getTienda());
                stmt.setString(3, item.getTerminal());
                stmt.setString(4, item.getTransaccion());
                stmt.setString(5, item.getFecha());
                stmt.setString(6, item.getNumeroOrden());
                
                // Parámetros para el UPDATE
                stmt.setDate(7, new java.sql.Date(item.getFechaCompleta().getTime())); // FECHACOMPLETA
                stmt.setLong(8, item.getTipo());
                stmt.setString(9, item.getNumTarjeta());
                stmt.setString(10, String.valueOf(item.getImporte()));
                stmt.setString(11, item.getVendedor());
                stmt.setString(12, item.getEsquema());
                stmt.setString(13, item.getNumAutorizacion());
                stmt.setString(14, item.getCodigoRespuesta());
                stmt.setLong(15, 1);
                stmt.setLong(16, 2);
                
             // Parámetros para el INSERT
                stmt.setLong(17, item.getConsecutivo());
                stmt.setString(18, item.getTienda());
                stmt.setString(19, item.getTerminal());
                stmt.setString(20, item.getTransaccion());
                stmt.setString(21, item.getFecha());
                stmt.setString(22, item.getNumeroOrden());
                stmt.setDate(23, new java.sql.Date(item.getFechaCompleta().getTime())); // FECHACOMPLETA
                stmt.setLong(24, item.getTipo());
                stmt.setString(25, item.getNumTarjeta());
                stmt.setString(26, String.valueOf(item.getImporte()));
                stmt.setString(27, item.getVendedor());
                stmt.setString(28, item.getEsquema());
                stmt.setString(29, item.getNumAutorizacion());
                stmt.setString(30, item.getCodigoRespuesta());
                stmt.setLong(31, 1);
                stmt.setLong(32, 2);

                // Ejecutar el MERGE
                stmt.executeUpdate();

                // Commit de la transacción
                connection.commit();
                System.out.println("Registros insertados o actualizados correctamente.");
            } catch (SQLException e) {
                connection.rollback();
                System.err.println("Error al insertar/actualizar registros: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.err.println("Error de conexión a la base de datos: " + e.getMessage());
        }
    }



}
