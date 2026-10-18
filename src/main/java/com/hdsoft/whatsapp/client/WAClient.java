package com.hdsoft.whatsapp.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hdsoft.whatsapp.constants.WhatsappURL;
import com.hdsoft.whatsapp.pojos.ContentMessage;
import com.hdsoft.whatsapp.pojos.ContentResponse;
import com.hdsoft.whatsapp.pojos.Error;
import com.hdsoft.whatsapp.pojos.Errorable;
import com.hdsoft.whatsapp.pojos.GroupInfo;
import com.hdsoft.whatsapp.pojos.GroupResponse;
import com.hdsoft.whatsapp.pojos.Message;
import com.hdsoft.whatsapp.pojos.MessageResponse;
import com.hdsoft.whatsapp.pojos.NumberCheckResult;
import com.hdsoft.whatsapp.pojos.PairingCodeResponse;
import com.hdsoft.whatsapp.pojos.QRResponse;
import com.hdsoft.whatsapp.pojos.Session;
import com.hdsoft.whatsapp.pojos.ResponseError;
import com.hdsoft.whatsapp.pojos.SessionPayload;
import com.hdsoft.whatsapp.util.Checks;
import com.hdsoft.whatsapp.util.WhatsappException;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class WAClient {

    private final Gson gs = new Gson();
    private final String apikey;

    public WAClient() {
        if (System.getProperty("whatsapp.base").isEmpty()) {
            throw new WhatsappException(500, "Base url not set");
        }

        if (System.getProperty("whatsapp.api.key").isEmpty()) {
            throw new WhatsappException(500, "No API Key Found");
        }

        this.apikey = System.getProperty("whatsapp.api.key");

        WhatsappURL.initialize();
    }

    public List<Session> sessions(Consumer<? super Errorable> error) {
        HttpResponse<String> response = Unirest.get(WhatsappURL.getListSession()).header("X-API-Key", this.apikey).header("Content-Type", "application/json").asString();

        return doInternalList(response, error, Session.class);
    }

    public Session createSession(SessionPayload payload, Consumer<? super Errorable> error) {
        HttpResponse<String> response = Unirest.post(WhatsappURL.getCreateSession()).header("X-API-Key", this.apikey).header("Content-Type", "application/json").body(payload.toString()).asString();

        return doInternal(response, error, Session.class);
    }

    public Session sessionInfo(String sessionId, Consumer<? super Errorable> error) {
        Checks.validateSession(sessionId);
        HttpResponse<String> response = Unirest.get(WhatsappURL.getSessionInfo(sessionId)).header("X-API-Key", this.apikey).header("Content-Type", "application/json").asString();

        return doInternal(response, error, Session.class);
    }

    public Session startSession(String sessionId, Consumer<? super Errorable> error) {
        Checks.validateSession(sessionId);
        HttpResponse<String> response = Unirest.post(WhatsappURL.getStartSession(sessionId)).header("X-API-Key", this.apikey).header("Content-Type", "application/json").asString();

        return doInternal(response, error, Session.class);
    }

    public QRResponse qrSession(String sessionId, Consumer<? super Errorable> error) {
        Checks.validateSession(sessionId);
        HttpResponse<String> response = Unirest.get(WhatsappURL.getQrSession(sessionId)).header("X-API-Key", this.apikey).header("Content-Type", "application/json").asString();

        return doInternal(response, error, QRResponse.class);
    }

    public Session stopSession(String sessionId, Consumer<? super Errorable> error) {
        Checks.validateSession(sessionId);
        HttpResponse<String> response = Unirest.post(WhatsappURL.getStopSession(sessionId)).header("X-API-Key", this.apikey).header("Content-Type", "application/json").asString();

        return doInternal(response, error, Session.class);
    }

    public Session logoutSession(String sessionId, Consumer<? super Errorable> error) {
        Checks.validateSession(sessionId);
        HttpResponse<String> response = Unirest.post(WhatsappURL.getLogoutSession(sessionId)).header("X-API-Key", this.apikey).header("Content-Type", "application/json").asString();

        return doInternal(response, error, Session.class);
    }

    public PairingCodeResponse pairingCodeSession(String sessionId, String phoneNumber, Consumer<? super Errorable> error) {
        Checks.validateSession(sessionId);
        Checks.validatePhone(phoneNumber);
        HttpResponse<String> response = Unirest.post(WhatsappURL.getPairingCodeSession(sessionId)).header("X-API-Key", this.apikey).header("Content-Type", "application/json").body(new JSONObject().put("phoneNumber", "91" + phoneNumber).toString()).asString();

        return doInternal(response, error, PairingCodeResponse.class);
    }

    public MessageResponse sendTextSession(String sessionId, Message message, Consumer<? super Errorable> error) {
        Checks.validateSession(sessionId);
        HttpResponse<String> response = Unirest.post(WhatsappURL.getSendText(sessionId)).header("X-API-Key", this.apikey).header("Content-Type", "application/json").body(new JSONObject(message).toString()).asString();
        return doInternal(response, error, MessageResponse.class);
    }

    public ContentResponse sendImage(String sessionId, ContentMessage message, Consumer<? super Errorable> error) {
        Checks.validateSession(sessionId);
        message.setCaption("");
        return sendImageText(sessionId, message, error);
    }

    public ContentResponse sendImageText(String sessionId, ContentMessage message, Consumer<? super Errorable> error) {
        Checks.validateSession(sessionId);

        HttpResponse<String> response = Unirest.post(WhatsappURL.getSendImageText(sessionId)).header("X-API-Key", this.apikey).header("Content-Type", "application/json").body(new JSONObject(message).put("chatId", message.getChatId()).put("base64", message.getUrl()).put("mimetype", message.getMimetype()).put("filename", message.getFilename()).put("caption", message.getCaption()).toString()).asString();
        return doInternal(response, error, ContentResponse.class);
    }

    public ContentResponse sendFile(String sessionId, ContentMessage message, Consumer<? super Errorable> error) {
        Checks.validateSession(sessionId);
        message.setCaption("");
        return sendFileText(sessionId, message, error);
    }

    public ContentResponse sendFileText(String sessionId, ContentMessage message, Consumer<? super Errorable> error) {
        Checks.validateSession(sessionId);
        HttpResponse<String> response = Unirest.post(WhatsappURL.getSendFileText(sessionId)).header("X-API-Key", this.apikey).header("Content-Type", "application/json").body(new JSONObject(message).put("chatId", message.getChatId()).put("base64", message.getUrl()).put("mimetype", message.getMimetype()).put("filename", message.getFilename()).put("caption", message.getCaption()).toString()).asString();
        return doInternal(response, error, ContentResponse.class);
    }

    public NumberCheckResult numberCheck(String sessionId, String phone, Consumer<? super Errorable> error) {
        Checks.validateSession(sessionId);
        Checks.validatePhone(phone);
        HttpResponse<String> response = Unirest.get(WhatsappURL.getContactAvailability(sessionId, phone)).header("X-API-Key", this.apikey).header("Content-Type", "application/json").asString();
        return doInternal(response, error, NumberCheckResult.class);
    }

    public List<GroupResponse> listGroups(String sessionId, Consumer<? super Errorable> error) {
        HttpResponse<String> response = Unirest.get(WhatsappURL.getGroups(sessionId)).header("X-API-Key", this.apikey).header("Content-Type", "application/json").asString();

        return doInternalList(response, error, GroupResponse.class);
    }

    public GroupInfo infoGroup(String sessionId, String groupId, Consumer<? super Errorable> error) {
        Checks.validateSession(sessionId);

        HttpResponse<String> response = Unirest.get(WhatsappURL.getGroupInfo(sessionId, groupId)).header("X-API-Key", this.apikey).header("Content-Type", "application/json").asString();

        return doInternal(response, error, GroupInfo.class);
    }

    private <T> T doInternal(HttpResponse<String> response, Consumer<? super Errorable> error, Class<T> _class) {
        if (response.isSuccess()) {
            return gs.fromJson(response.getBody(), _class);
        } else {
            try {
                ResponseError sessionError = gs.fromJson(response.getBody(), ResponseError.class);
                error.accept(sessionError);
            } catch (Exception e) {
                Error errorx = gs.fromJson(response.getBody(), new TypeToken<Error>() {
                }.getType());
                error.accept(errorx);
            }

            try {
                return _class.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                error.accept(new Error(e.getMessage(), e.getCause().getMessage(), 500));
                return null;
            }
        }
    }

    private <T> List<T> doInternalList(HttpResponse<String> response, Consumer<? super Errorable> error, Class<T> _class) {
        if (response.isSuccess()) {
            Type listType = TypeToken.getParameterized(List.class, _class).getType();
            List<T> list = gs.fromJson(response.getBody(), listType);

            return list != null ? list : new ArrayList<>();
        }
        try {
            error.accept(gs.fromJson(response.getBody(), ResponseError.class));
        } catch (Exception e) {
            error.accept(new Error(response.getBody(), "HTTP " + response.getStatus(), response.getStatus()));
        }
        return new ArrayList<>();
    }
}
