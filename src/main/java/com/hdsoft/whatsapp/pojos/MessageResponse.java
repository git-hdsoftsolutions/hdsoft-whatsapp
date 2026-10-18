package com.hdsoft.whatsapp.pojos;

import com.google.gson.Gson;

import java.lang.management.GarbageCollectorMXBean;

public class MessageResponse {

    private String messageId;
    private long timestamp;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
