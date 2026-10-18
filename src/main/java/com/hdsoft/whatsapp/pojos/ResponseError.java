package com.hdsoft.whatsapp.pojos;

import com.google.gson.Gson;

import java.util.ArrayList;

class PageError {
    private String name;
    private String message;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

public class ResponseError extends Errorable{
    private int statusCode;
    private String message;
    private String error;
    private String code;
    private int retryAfterSeconds;
    private ArrayList<String> validOptions;
    private PageError pageError;
    private String build;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getRetryAfterSeconds() {
        return retryAfterSeconds;
    }

    public void setRetryAfterSeconds(int retryAfterSeconds) {
        this.retryAfterSeconds = retryAfterSeconds;
    }

    public ArrayList<String> getValidOptions() {
        return validOptions;
    }

    public void setValidOptions(ArrayList<String> validOptions) {
        this.validOptions = validOptions;
    }

    public PageError getPageError() {
        return pageError;
    }

    public void setPageError(PageError pageError) {
        this.pageError = pageError;
    }

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
