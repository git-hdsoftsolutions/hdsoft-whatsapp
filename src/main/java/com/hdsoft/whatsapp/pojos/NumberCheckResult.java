package com.hdsoft.whatsapp.pojos;

import com.google.gson.Gson;

public class NumberCheckResult {

    private String number;
    private boolean exists;
    private String whatsappId;

    public NumberCheckResult(String number, boolean exists, String whatsappId) {
        this.number = number;
        this.exists = exists;
        this.whatsappId = whatsappId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public String getWhatsappId() {
        return whatsappId;
    }

    public void setWhatsappId(String whatsappId) {
        this.whatsappId = whatsappId;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
