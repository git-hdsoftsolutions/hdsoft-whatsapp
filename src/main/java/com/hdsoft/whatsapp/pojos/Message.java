package com.hdsoft.whatsapp.pojos;

import com.google.gson.Gson;

public class Message {

    private String chatId;
    private String text;

    public Message(String chatId, String text) {
        this.chatId = chatId+"@c.us";
        this.text = text;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
