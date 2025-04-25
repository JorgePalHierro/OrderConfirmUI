package com.example.Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class Log {
    private String logFileName;

    // Método para obtener el nombre del archivo con la fecha actual
    private String generarNombreArchivo() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        String fecha = dateFormat.format(new Date());
        return "log_" + fecha + ".txt";
    }

    // Verifica si el archivo existe, si no, lo crea
    private void verificarOCrearArchivo() {
        logFileName = generarNombreArchivo();
        File archivo = new File(logFileName);
        if (!archivo.exists()) {
            try {
                if (archivo.createNewFile()) {
                    System.out.println("Archivo creado: " + logFileName);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para escribir en el log
    public void escribirLog(String mensaje) {
        verificarOCrearArchivo();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFileName, true))) {
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String fechaHora = dateTimeFormat.format(new Date());
            writer.write(fechaHora + " - " + mensaje);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} 
