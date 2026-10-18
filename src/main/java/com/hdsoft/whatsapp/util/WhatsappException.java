package com.hdsoft.whatsapp.util;

import java.io.IOException;

public final class WhatsappException extends RuntimeException {

    private final int statusCode;
    private final String responseBody;

    public WhatsappException(int statusCode, String responseBody) {
        super("OpenWA HTTP " + statusCode + ": " + responseBody);

        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }
}