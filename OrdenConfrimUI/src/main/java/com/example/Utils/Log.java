package com.example.Utils;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
@Component
public class Log {
    private String logFileName;

    // Constructor: crea el nombre del archivo con la fecha actual
    
    public Log() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        String fecha = dateFormat.format(new Date());
        this.logFileName = "log_" + fecha + ".txt";
    }

    // Método para escribir en el log
    public void escribirLog(String mensaje) {
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
