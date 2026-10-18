package com.hdsoft.whatsapp.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
import com.hdsoft.whatsapp.util.WhatsappException;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.json.JSONObject;
import sun.misc.resources.Messages_es;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
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
        HttpResponse<String> response = Unirest.get(WhatsappURL.getListSession())
                .header("X-API-Key", this.apikey)
                .header("Content-Type", "application/json")
                .asString();

        if (response.isSuccess()) {
            List<Session> o = gs.fromJson(response.getBody(), new TypeToken<List<Session>>() {
            }.getType());

            return o;
        } else {
            try {
                ResponseError sessionError = gs.fromJson(response.getBody(), new TypeToken<List<ResponseError>>() {
                }.getType());
                error.accept(sessionError);
            } catch (Exception e) {
                Error errorx = gs.fromJson(response.getBody(), new TypeToken<Error>() {
                }.getType());
                error.accept(errorx);
            }
            return new ArrayList<>();
        }
    }

    public Session createSession(SessionPayload payload, Consumer<? super Errorable> error) {
        HttpResponse<String> response = Unirest.post(WhatsappURL.getCreateSession())
                .header("X-API-Key", this.apikey)
                .header("Content-Type", "application/json")
                .body(payload.toString())
                .asString();

        if (response.isSuccess()) {
            return gs.fromJson(response.getBody(), Session.class);
        } else {
            System.out.println(response.getBody());
            Errorable sessionError = null;
            try {
                sessionError = gs.fromJson(response.getBody(), new TypeToken<List<ResponseError>>() {
                }.getType());
            } catch (Exception e) {
                sessionError = gs.fromJson(response.getBody(), new TypeToken<List<Error>>() {
                }.getType());
            }
            error.accept(sessionError);
            return new Session();
        }
    }

    public Session sessionInfo(String sessionId, Consumer<? super Errorable> error) {
        HttpResponse<String> response = Unirest.get(WhatsappURL.getSessionInfo(sessionId))
                .header("X-API-Key", this.apikey)
                .header("Content-Type", "application/json")
                .asString();

        if (response.isSuccess()) {
            return gs.fromJson(response.getBody(), Session.class);
        } else {
            try {
                ResponseError sessionError = gs.fromJson(response.getBody(), new TypeToken<List<ResponseError>>() {
                }.getType());
                error.accept(sessionError);
            } catch (Exception e) {
                Error errorx = gs.fromJson(response.getBody(), new TypeToken<Error>() {
                }.getType());
                error.accept(errorx);
            }
            return new Session();
        }
    }

    public Session startSession(String sessionId, Consumer<? super Errorable> error) {
        HttpResponse<String> response = Unirest.post(WhatsappURL.getStartSession(sessionId))
                .header("X-API-Key", this.apikey)
                .header("Content-Type", "application/json")
                .asString();

        if (response.isSuccess()) {
            return gs.fromJson(response.getBody(), Session.class);
        } else {
            try {
                ResponseError sessionError = gs.fromJson(response.getBody(), new TypeToken<List<ResponseError>>() {
                }.getType());
                error.accept(sessionError);
            } catch (Exception e) {
                Error errorx = gs.fromJson(response.getBody(), new TypeToken<Error>() {
                }.getType());
                error.accept(errorx);
            }
            return new Session();
        }
    }

    public QRResponse qrSession(String sessionId, Consumer<? super Errorable> error) {
        HttpResponse<String> response = Unirest.get(WhatsappURL.getQrSession(sessionId))
                .header("X-API-Key", this.apikey)
                .header("Content-Type", "application/json")
                .asString();

        if (response.isSuccess()) {
            return gs.fromJson(response.getBody(), QRResponse.class);
        } else {
            try {
                ResponseError sessionError = gs.fromJson(response.getBody(), new TypeToken<List<ResponseError>>() {
                }.getType());
                error.accept(sessionError);
            } catch (Exception e) {
                Error errorx = gs.fromJson(response.getBody(), new TypeToken<Error>() {
                }.getType());
                error.accept(errorx);
            }
            return new QRResponse();
        }
    }

    public Session stopSession(String sessionId, Consumer<? super Errorable> error) {
        HttpResponse<String> response = Unirest.post(WhatsappURL.getStopSession(sessionId))
                .header("X-API-Key", this.apikey)
                .header("Content-Type", "application/json")
                .asString();

        if (response.isSuccess()) {
            return gs.fromJson(response.getBody(), Session.class);
        } else {
            try {
                ResponseError sessionError = gs.fromJson(response.getBody(), new TypeToken<List<ResponseError>>() {
                }.getType());
                error.accept(sessionError);
            } catch (Exception e) {
                Error errorx = gs.fromJson(response.getBody(), new TypeToken<Error>() {
                }.getType());
                error.accept(errorx);
            }
            return new Session();
        }
    }

    public Session logoutSession(String sessionId, Consumer<? super Errorable> error) {
        HttpResponse<String> response = Unirest.post(WhatsappURL.getLogoutSession(sessionId))
                .header("X-API-Key", this.apikey)
                .header("Content-Type", "application/json")
                .asString();

        if (response.isSuccess()) {
            return gs.fromJson(response.getBody(), Session.class);
        } else {
            try {
                ResponseError sessionError = gs.fromJson(response.getBody(), new TypeToken<List<ResponseError>>() {
                }.getType());
                error.accept(sessionError);
            } catch (Exception e) {
                Error errorx = gs.fromJson(response.getBody(), new TypeToken<Error>() {
                }.getType());
                error.accept(errorx);
            }
            return new Session();
        }
    }

    public PairingCodeResponse pairingCodeSession(String sessionId, String phoneNumber, Consumer<? super Errorable> error) {
        HttpResponse<String> response = Unirest.post(WhatsappURL.getPairingCodeSession(sessionId))
                .header("X-API-Key", this.apikey)
                .header("Content-Type", "application/json")
                .body(new JSONObject().put("phoneNumber", phoneNumber).toString())
                .asString();

        if (response.isSuccess()) {
            return gs.fromJson(response.getBody(), PairingCodeResponse.class);
        } else {
            try {
                ResponseError sessionError = gs.fromJson(response.getBody(), new TypeToken<List<ResponseError>>() {
                }.getType());
                error.accept(sessionError);
            } catch (Exception e) {
                Error errorx = gs.fromJson(response.getBody(), new TypeToken<Error>() {
                }.getType());
                error.accept(errorx);
            }
            return new PairingCodeResponse();
        }
    }

    public MessageResponse sendTextSession(String sessionId, Message message, Consumer<? super Errorable> error) {
        HttpResponse<String> response = Unirest.post(WhatsappURL.getSendText(sessionId))
                .header("X-API-Key", this.apikey)
                .header("Content-Type", "application/json")
                .body(new JSONObject(message).toString())
                .asString();
        if (response.isSuccess()) {
            return gs.fromJson(response.getBody(), MessageResponse.class);
        } else {
            try {
                ResponseError sessionError = gs.fromJson(response.getBody(), new TypeToken<List<ResponseError>>() {
                }.getType());
                error.accept(sessionError);
            } catch (Exception e) {
                Error errorx = gs.fromJson(response.getBody(), new TypeToken<Error>() {
                }.getType());
                error.accept(errorx);
            }
            return new MessageResponse();
        }
    }

    public ContentResponse sendImage(String sessionId, ContentMessage message, Consumer<? super Errorable> error) {
        message.setCaption("");
        return sendImageText(sessionId, message, error);
    }

    public ContentResponse sendImageText(String sessionId, ContentMessage message, Consumer<? super Errorable> error) {
        System.out.println(message.getCaption());
        HttpResponse<String> response = Unirest.post(WhatsappURL.getSendImageText(sessionId))
                .header("X-API-Key", this.apikey)
                .header("Content-Type", "application/json")
                .body(new JSONObject(message)
                        .put("chatId",message.getChatId())
                        .put("base64",message.getUrl())
                        .put("mimetype",message.getMimetype())
                        .put("filename",message.getFilename())
                        .put("caption", message.getCaption())
                        .toString())
                .asString();
        if (response.isSuccess()) {
            return gs.fromJson(response.getBody(), ContentResponse.class);
        } else {
            try {
                ResponseError sessionError = gs.fromJson(response.getBody(), new TypeToken<List<ResponseError>>() {
                }.getType());
                error.accept(sessionError);
            } catch (Exception e) {
                Error errorx = gs.fromJson(response.getBody(), new TypeToken<Error>() {
                }.getType());
                error.accept(errorx);
            }
            return new ContentResponse("",-1);
        }
    }

    public ContentResponse sendFile(String sessionId, ContentMessage message, Consumer<? super Errorable> error) {
        message.setCaption("");
        return sendFileText(sessionId, message, error);
    }

    public ContentResponse sendFileText(String sessionId, ContentMessage message, Consumer<? super Errorable> error) {
        System.out.println(message.getCaption());
        HttpResponse<String> response = Unirest.post(WhatsappURL.getSendFileText(sessionId))
                .header("X-API-Key", this.apikey)
                .header("Content-Type", "application/json")
                .body(new JSONObject(message)
                        .put("chatId",message.getChatId())
                        .put("base64",message.getUrl())
                        .put("mimetype",message.getMimetype())
                        .put("filename",message.getFilename())
                        .put("caption", message.getCaption())
                        .toString())
                .asString();
        if (response.isSuccess()) {
            return gs.fromJson(response.getBody(), ContentResponse.class);
        } else {
            try {
                ResponseError sessionError = gs.fromJson(response.getBody(), new TypeToken<List<ResponseError>>() {
                }.getType());
                error.accept(sessionError);
            } catch (Exception e) {
                Error errorx = gs.fromJson(response.getBody(), new TypeToken<Error>() {
                }.getType());
                error.accept(errorx);
            }
            return new ContentResponse("",-1);
        }
    }

    public NumberCheckResult numberCheck(String sessionId, String phone, Consumer<? super Errorable> error) {
        HttpResponse<String> response = Unirest.get(WhatsappURL.getContactAvailability(sessionId,phone))
                .header("X-API-Key", this.apikey)
                .header("Content-Type", "application/json")
                .asString();
        if (response.isSuccess()) {
            return gs.fromJson(response.getBody(), NumberCheckResult.class);
        } else {
            try {
                ResponseError sessionError = gs.fromJson(response.getBody(), new TypeToken<List<ResponseError>>() {
                }.getType());
                error.accept(sessionError);
            } catch (Exception e) {
                Error errorx = gs.fromJson(response.getBody(), new TypeToken<Error>() {
                }.getType());
                error.accept(errorx);
            }
            return new NumberCheckResult("",false,"");
        }
    }

    public List<GroupResponse> listGroups(String sessionId, Consumer<? super Errorable> error) {
        HttpResponse<String> response = Unirest.get(WhatsappURL.getGroups(sessionId))
                .header("X-API-Key", this.apikey)
                .header("Content-Type", "application/json")
                .asString();

        if (response.isSuccess()) {
            return gs.fromJson(response.getBody(), new TypeToken<List<GroupResponse>>(){}.getType());
        } else {
            try {
                ResponseError sessionError = gs.fromJson(response.getBody(), new TypeToken<List<ResponseError>>() {
                }.getType());
                error.accept(sessionError);
            } catch (Exception e) {
                Error errorx = gs.fromJson(response.getBody(), new TypeToken<Error>() {
                }.getType());
                error.accept(errorx);
            }
            return new ArrayList<>();
        }
    }

    public GroupInfo infoGroup(String sessionId, String groupId, Consumer<? super Errorable> error) {
        HttpResponse<String> response = Unirest.get(WhatsappURL.getGroupInfo(sessionId,groupId))
                .header("X-API-Key", this.apikey)
                .header("Content-Type", "application/json")
                .asString();

        if (response.isSuccess()) {
            return gs.fromJson(response.getBody(), GroupInfo.class);
        } else {
            try {
                ResponseError sessionError = gs.fromJson(response.getBody(), new TypeToken<List<ResponseError>>() {
                }.getType());
                error.accept(sessionError);
            } catch (Exception e) {
                Error errorx = gs.fromJson(response.getBody(), new TypeToken<Error>() {
                }.getType());
                error.accept(errorx);
            }
            return new GroupInfo();
        }
    }
}
