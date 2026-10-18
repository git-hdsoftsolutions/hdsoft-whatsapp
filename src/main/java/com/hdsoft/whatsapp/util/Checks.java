package com.hdsoft.whatsapp.util;

import java.util.UUID;

public class Checks {

    public static void validateSession(String sessionID) {
        if (sessionID.isEmpty()) {
            throw new WhatsappException(500, "Session ID cannot be empty");
        }

        if (!sessionID.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")) {
            throw new WhatsappException(500, "Invalid Session ID");
        }
    }

    public static void validatePhone(String phone) {
        if (phone.length()!=10) {
            throw new WhatsappException(500, "Phone numbers is not of 10 digits");
        }

        if(!phone.matches("^[6-9]\\d{9}$")){
            throw new WhatsappException(500, "Phone numbers is invalid");
        }
    }

}
