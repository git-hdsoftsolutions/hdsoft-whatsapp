package com.hdsoft.whatsapp.pojos;

public class QRResponse {
    private String qrCode;
    private String status;

    public QRResponse() {
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
