package com.hdsoft.whatsapp.pojos;

import com.google.gson.Gson;

public class Message {

    private String chatId;
    private String text;

    public Message() {
    }

    public Message(String chatId, String text) {
        this.chatId = "91" + chatId + "@c.us";
        this.text = text;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public void setGChatId(String chatId) {
        this.chatId = chatId + "@g.us";
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
