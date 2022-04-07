package com.eneyeitech.maintenance_service_api.utility;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Helper {
    public static String getCurrentDateTime() {
        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return today.format(formatter);
    }

    public static String getNewUUID(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
