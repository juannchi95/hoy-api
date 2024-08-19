package com.jalfonso.hoy_api.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jalfonso.hoy_api.exceptions.InternalServerExcepcion;


public class Util {

    private Util() {}

    private static final Logger log = LoggerFactory.getLogger(Util.class);

    public static final String INTERNAL_SERVER_ERROR = "Error interno del servidor";
    public static final String BAD_REQUEST = "Parametros invalidos";
    public static final String NOT_FOUND = "No se encuentran noticias para el texto: ";

    public static HashMap<String, String> getPhotoDataMap(String photoLink) {
        HashMap<String, String> photoDatMap = new HashMap<>();
        try {
            URL urlPhoto = new URL(photoLink);
            HttpURLConnection connection = (HttpURLConnection) urlPhoto.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(300000);
            
            photoDatMap.put("type", connection.getContentType());

            InputStream inputStream = connection.getInputStream();
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
            byte[] buffer = new byte[8192];  
            int len = 0;  
            while ((len = inputStream.read(buffer)) != -1) {  
                outStream.write(buffer, 0, len);  
            }  
            inputStream.close(); 
            
            try {
                byte[] imageBytes = outStream.toByteArray();
                photoDatMap.put("photoEncode", java.util.Base64.getEncoder().encodeToString(imageBytes));
            } catch (Exception e) {
                log.error("Error al convertir imagen a base64: {}", e.getMessage());
                throw new InternalServerExcepcion("g100", INTERNAL_SERVER_ERROR);
            }

            return photoDatMap;
        } catch (Exception e) {
            log.error("Error al obtener imagen desde el servidor: {}", e.getMessage());
            throw new InternalServerExcepcion("g100", INTERNAL_SERVER_ERROR);
        }
    }

    public static String formatDateTime(String dateTimeStr) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy HH:mm", new Locale("es", "ES"));
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime;

        try {
            if(dateTimeStr.contains("hora") || dateTimeStr.contains("minuto")) {
                LocalDateTime now = LocalDateTime.now();
                String[] parts = dateTimeStr.split(" ");
                int amount = Integer.parseInt(parts[1]);
                String unit = parts[2];
                
                if (unit.contains("hora")) {
                    now = now.minus(amount, ChronoUnit.HOURS);
                } else {
                    now = now.minus(amount, ChronoUnit.MINUTES);
                }
                dateTime = now;
            } else {
                dateTimeStr = dateTimeStr.contains(":") ? dateTimeStr : dateTimeStr + " 00:00";
                dateTime = LocalDateTime.parse(dateTimeStr, inputFormatter);
            } 

            return dateTime.format(outputFormatter);
        } catch (DateTimeParseException e) {
            log.error("Error al formatear fecha: {}", e.getMessage());
            return "";
        }
    }
}
