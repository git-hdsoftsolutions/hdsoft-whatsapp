package com.hdsoft.whatsapp.pojos;

public class ContentMessage {

    private String chatId;
    private String url;

    private String filename;
    private String mimetype;
    private String caption;

    public ContentMessage() {
    }

    public ContentMessage(String chatId, String url, String filename, String mimetype, String caption) {
        this.chatId = chatId+"@c.us";
        this.url = url;
        this.filename = filename;
        this.mimetype = mimetype;
        this.caption = caption;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}
