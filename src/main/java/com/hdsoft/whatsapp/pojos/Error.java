package com.hdsoft.whatsapp.pojos;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Error extends Errorable{
    public String message;
    public String error;
    public int statusCode;

    public Error() {
    }

    public Error(String message, String error, int statusCode) {
        this.message = message;
        this.error = error;
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
