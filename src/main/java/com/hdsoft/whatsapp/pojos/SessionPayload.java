package com.hdsoft.whatsapp.pojos;

import com.google.gson.Gson;

class Config{
    private boolean autoRejectCalls;
    private int maxReconnectAttempts;
    private int reconnectBaseDelay;

    public Config(boolean autoRejectCalls, int maxReconnectAttempts, int reconnectBaseDelay) {
        this.autoRejectCalls = autoRejectCalls;
        this.maxReconnectAttempts = maxReconnectAttempts;
        this.reconnectBaseDelay = reconnectBaseDelay;
    }

    public boolean isAutoRejectCalls() {
        return autoRejectCalls;
    }

    public void setAutoRejectCalls(boolean autoRejectCalls) {
        this.autoRejectCalls = autoRejectCalls;
    }

    public int getMaxReconnectAttempts() {
        return maxReconnectAttempts;
    }

    public void setMaxReconnectAttempts(int maxReconnectAttempts) {
        this.maxReconnectAttempts = maxReconnectAttempts;
    }

    public int getReconnectBaseDelay() {
        return reconnectBaseDelay;
    }

    public void setReconnectBaseDelay(int reconnectBaseDelay) {
        this.reconnectBaseDelay = reconnectBaseDelay;
    }
}

public class SessionPayload {
    private String name;
    private Config config;
   // private String proxyUrl;

    public SessionPayload(String name, boolean autoRejectCalls, int maxReconnectAttempts, int reconnectBaseDelay, String proxyUrl) {
        this.name = name;
        this.config = new Config(autoRejectCalls, maxReconnectAttempts, reconnectBaseDelay);
        //this.proxyUrl = proxyUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    /*
    public String getProxyUrl() {
        return proxyUrl;
    }

    public void setProxyUrl(String proxyUrl) {
        this.proxyUrl = proxyUrl;
    }
*/

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}