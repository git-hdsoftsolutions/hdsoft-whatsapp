package com.hdsoft.whatsapp.constants;

public class WhatsappURL {

    private static final String listSession = "/api/sessions";
    private static final String createSession = "/api/sessions";
    private static final String sessionInfo = "/api/sessions/{sessionId}";
    private static final String startSession = "/api/sessions/{sessionId}/start";
    private static final String qrSession = "/api/sessions/{sessionId}/qr";
    private static final String pairingCodeSession = "/api/sessions/{sessionId}/pairing-code";
    private static final String stopSession = "/api/sessions/{sessionId}/stop";
    private static final String logoutSession = "/api/sessions/{sessionId}/logout   ";
    private static final String sendText = "/api/sessions/{sessionId}/messages/send-text";
    private static final String sendImageText = "/api/sessions/{sessionId}/messages/send-image";
    private static final String sendFileText = "/api/sessions/{sessionId}/messages/send-document";
    private static final String groups = "/api/sessions/{sessionId}/groups";
    private static final String groupInfo = "/api/sessions/{sessionId}/groups/{groupId}";
    private static final String availability = "/api/sessions/{sessionId}/contacts/check/{number}";
    private static String baseUrl;

    public static void initialize() {
        baseUrl = System.getProperty("whatsapp.base");
    }

    public static String getListSession() {
        return (baseUrl + listSession.trim()).replaceAll("/+$", "");
    }

    public static String getCreateSession() {
        return (baseUrl + createSession.trim()).replaceAll("/+$", "");
    }

    public static String getSessionInfo(String defaultSessionId) {
        return (baseUrl + sessionInfo.replace("{sessionId}", defaultSessionId)).trim().replaceAll("/+$", "");
    }

    public static String getStartSession(String defaultSessionId) {
        return (baseUrl + startSession.replace("{sessionId}", defaultSessionId)).trim().replaceAll("/+$", "");
    }

    public static String getQrSession(String defaultSessionId) {
        return (baseUrl + qrSession.replace("{sessionId}", defaultSessionId)).trim().replaceAll("/+$", "");
    }

    public static String getPairingCodeSession(String defaultSessionId) {
        return (baseUrl + pairingCodeSession.replace("{sessionId}", defaultSessionId)).trim().replaceAll("/+$", "");
    }

    public static String getStopSession(String defaultSessionId) {
        return (baseUrl + stopSession.replace("{sessionId}", defaultSessionId)).trim().replaceAll("/+$", "");
    }

    public static String getLogoutSession(String defaultSessionId) {
        return (baseUrl + logoutSession.replace("{sessionId}", defaultSessionId)).trim().replaceAll("/+$", "");
    }

    public static String getSendText(String defaultSessionId) {
        return (baseUrl + sendText.replace("{sessionId}", defaultSessionId)).trim().replaceAll("/+$", "");
    }

    public static String getSendImageText(String defaultSessionId) {
        return (baseUrl + sendImageText.replace("{sessionId}", defaultSessionId)).trim().replaceAll("/+$", "");
    }

    public static String getSendFileText(String defaultSessionId) {
        return (baseUrl + sendFileText.replace("{sessionId}", defaultSessionId)).trim().replaceAll("/+$", "");
    }

    public static String getGroups(String defaultSessionId) {
        return (baseUrl + groups.replace("{sessionId}", defaultSessionId)).trim().replaceAll("/+$", "");
    }

    public static String getGroupInfo(String defaultSessionId, String groupId) {
        return (baseUrl + groupInfo.replace("{sessionId}", defaultSessionId).replace("{groupId}", groupId)).trim().replaceAll("/+$", "");
    }

    public static String getContactAvailability(String defaultSessionId, String number) {
        return (baseUrl + availability.replace("{sessionId}", defaultSessionId).replace("{number}", number)).trim().replaceAll("/+$", "");
    }
}
