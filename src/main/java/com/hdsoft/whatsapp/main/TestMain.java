package com.hdsoft.whatsapp.main;

import com.google.gson.stream.JsonToken;
import com.hdsoft.whatsapp.client.WAClient;
import com.hdsoft.whatsapp.pojos.ContentMessage;
import com.hdsoft.whatsapp.pojos.ContentResponse;
import com.hdsoft.whatsapp.pojos.GroupInfo;
import com.hdsoft.whatsapp.pojos.GroupResponse;
import com.hdsoft.whatsapp.pojos.Message;
import com.hdsoft.whatsapp.pojos.MessageResponse;
import com.hdsoft.whatsapp.pojos.NumberCheckResult;
import com.hdsoft.whatsapp.pojos.PairingCodeResponse;
import com.hdsoft.whatsapp.pojos.QRResponse;
import com.hdsoft.whatsapp.pojos.Session;
import com.hdsoft.whatsapp.pojos.SessionPayload;

import javax.imageio.ImageIO;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;

public class TestMain {

    public static void main(String[] args) throws IOException {
        System.setProperty("whatsapp.base", "http://localhost:2785");
        System.setProperty("whatsapp.api.key", "owa_k1_ccc70ad235a4fcfa7741fb02d5b71696d3331a304d3739cdcf678bc976d8a286");

        File file = new File("C:\\Users\\hrish\\Downloads\\HDSOFT Neon Tech Solutions Banner-3.png");
        File pdf = new File("C:\\Users\\hrish\\Downloads\\ACK544552950010825.pdf");

        Scanner sc = new Scanner(System.in);

        WAClient client = new WAClient();

        while (true) {
            System.out.println("Whatsapp API Demo");
            System.out.println("0. Exit");
            System.out.println("1. List All Sessions");
            System.out.println("2. Create Session");
            System.out.println("3. Session Info");
            System.out.println("4. Start Session");
            System.out.println("5. Session QR");
            System.out.println("6. Stop Session");
            System.out.println("7. Logout Session");
            System.out.println("8. Pairing Code Session");
            System.out.println("9. Set Default Session");
            System.out.println("10. Send Text");
            System.out.println("11. Send Image");
            System.out.println("12. Send Image with Caption");
            System.out.println("13. Send File");
            System.out.println("14. Send File with Caption");
            System.out.println("15. Check Number Existence");
            System.out.println("16. List Groups");
            System.out.println("17. Group Info");
            System.out.println("18. Send Group Message");

            System.out.println("Enter choice:  ");
            int c = sc.nextInt();
            switch (c) {
                case 0: {
                    System.exit(0);
                    break;
                }
                case 1: {
                    List<Session> sessions = client.sessions(cx -> System.out.println(cx.toString()));
                    sessions.forEach(cy -> System.out.println(cy.json()));
                    break;
                }
                case 2: {
                    boolean autoRejectCalls;
                    int maxReconnectAttempts, reconnectBaseDelay;
                    String name, proxyurl;

                    System.out.println("\t\t autoRejectCalls: \t");
                    autoRejectCalls = sc.nextBoolean();

                    System.out.println("\t\t maxReconnectAttempts, reconnectBaseDelay : \t");
                    maxReconnectAttempts = sc.nextShort();
                    reconnectBaseDelay = sc.nextInt();

                    System.out.println("\t\t name, proxyurl : \t");
                    name = sc.next().trim();
                    proxyurl = "";

                    Session session = client.createSession(new SessionPayload(name, autoRejectCalls, maxReconnectAttempts, reconnectBaseDelay, proxyurl), cx -> System.out.println(cx.toString()));
                    System.out.println(session.json());

                    break;
                }
                case 3: {
                    String sessionId;
                    System.out.println("Enter Session ID:  ");
                    sessionId = sc.next().trim();

                    Session session = client.sessionInfo(sessionId, cx -> System.out.println(cx.toString()));
                    System.out.println(session.json());

                    break;
                }
                case 4: {
                    String sessionId;
                    System.out.println("Enter Session ID:  ");
                    sessionId = sc.next().trim();

                    Session session = client.startSession(sessionId, cx -> System.out.println(cx.toString()));
                    System.out.println(session.json());

                    break;
                }
                case 5: {
                    String sessionId;
                    System.out.println("Enter Session ID:  ");
                    sessionId = sc.next().trim();

                    QRResponse qr = client.qrSession(sessionId, cx -> System.out.println(cx.toString()));

                    if (qr.getStatus().equalsIgnoreCase("qr_ready")) {
                        String data = qr.getQrCode().split(",")[1];
                        byte[] decode = Base64.getDecoder().decode(data);
                        BufferedImage read = ImageIO.read(new ByteArrayInputStream(decode));
                        ImageIO.write(read, "PNG", Files.newOutputStream(Paths.get("qr_code.png")));
                        Desktop.getDesktop().open(Paths.get("qr_code.png").toFile());
                    } else {
                        System.out.println("QR Not Ready");
                    }
                    break;
                }
                case 6: {
                    String sessionId;
                    System.out.println("Enter Session ID:  ");
                    sessionId = sc.next().trim();

                    Session session = client.stopSession(sessionId, cx -> System.out.println(cx.toString()));
                    System.out.println(session.json());

                    break;
                }
                case 7: {
                    String sessionId;
                    System.out.println("Enter Session ID:  ");
                    sessionId = sc.next().trim();

                    Session session = client.logoutSession(sessionId, cx -> System.out.println(cx.toString()));
                    System.out.println(session.json());

                    break;
                }
                case 8: {
                    String sessionId, phonenumber;
                    System.out.println("Enter Session ID:  ");
                    sessionId = sc.next().trim();
                    System.out.println("Enter Phone Number:  ");
                    phonenumber = sc.next().trim();

                    PairingCodeResponse qr = client.pairingCodeSession(sessionId, phonenumber, cx -> System.out.println(cx.toString()));

                    if (qr.getStatus().equalsIgnoreCase("qr_ready")) {
                        System.out.println("Pairing Code:   " + qr.getPairingCode());
                    } else {
                        System.out.println("Pairing Code Not Ready");
                    }
                    break;
                }
                case 9: {
                    String sessionId;
                    System.out.println("Enter Session ID:  ");
                    sessionId = sc.next().trim();

                    System.setProperty("whatsapp.session.default.uuid", sessionId);

                    break;
                }
                case 10: {
                    String contact, msg;

                    System.out.println("Enter Contact:  ");
                    contact = sc.next().trim();

                    System.out.println("Enter message:   ");
                    msg = sc.next().trim();

                    MessageResponse messageResponse = client.sendTextSession(System.getProperty("whatsapp.session.default.uuid"), new Message(contact, msg), cx -> System.out.println(cx.toString()));
                    if (!messageResponse.getMessageId().isEmpty()) {
                        System.out.println("Message Sent:  " + messageResponse.getMessageId());
                    }

                    break;
                }
                case 11: {
                    String contact;

                    System.out.println("Enter Contact:  ");
                    contact = sc.next().trim();

                    String data = Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get(file.toURI())));

                    ContentMessage message = new ContentMessage("91" + contact, data, file.getName(), "image/png", "");


                    ContentResponse messageResponse = client.sendImage(System.getProperty("whatsapp.session.default.uuid"), message, cx -> System.out.println(cx.toString()));
                    if (!messageResponse.getMessageId().isEmpty()) {
                        System.out.println("Message Sent:  " + messageResponse.getMessageId());
                    }

                    break;
                }
                case 12: {
                    String contact, msg;

                    System.out.println("Enter Contact:  ");
                    contact = sc.next().trim();

                    msg = "I Love You !!!!!";

                    String data = Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get(file.toURI())));

                    ContentMessage message = new ContentMessage("91" + contact, data, file.getName(), "image/png", msg);

                    ContentResponse messageResponse = client.sendImageText(System.getProperty("whatsapp.session.default.uuid"), message, cx -> System.out.println(cx.toString()));
                    if (!messageResponse.getMessageId().isEmpty()) {
                        System.out.println("Message Sent:  " + messageResponse.getMessageId());
                    }

                    break;
                }
                case 13: {
                    String contact, msg;

                    System.out.println("Enter Contact:  ");
                    contact = sc.next().trim();

                    msg = "I Love You !!!!!";

                    String data = Base64.getEncoder().encodeToString(Files.readAllBytes(pdf.toPath()));

                    ContentMessage message = new ContentMessage("91" + contact, data, pdf.getName(), "application/pdf", msg);

                    ContentResponse messageResponse = client.sendFile(System.getProperty("whatsapp.session.default.uuid"), message, cx -> System.out.println(cx.toString()));
                    if (!messageResponse.getMessageId().isEmpty()) {
                        System.out.println("Message Sent:  " + messageResponse.getMessageId());
                    }

                    break;
                }
                case 14: {
                    String contact, msg;

                    System.out.println("Enter Contact:  ");
                    contact = sc.next().trim();

                    msg = "I Love You !!!!!";

                    String data = Base64.getEncoder().encodeToString(Files.readAllBytes(pdf.toPath()));

                    ContentMessage message = new ContentMessage("91" + contact, data, pdf.getName(), "application/pdf", msg);

                    ContentResponse messageResponse = client.sendFileText(System.getProperty("whatsapp.session.default.uuid"), message, cx -> System.out.println(cx.toString()));
                    if (!messageResponse.getMessageId().isEmpty()) {
                        System.out.println("Message Sent:  " + messageResponse.getMessageId());
                    }

                    break;
                }
                case 15: {
                    String contact;

                    System.out.println("Enter Contact:  ");
                    contact = sc.next().trim();

                    NumberCheckResult messageResponse = client.numberCheck(System.getProperty("whatsapp.session.default.uuid"), contact, cx -> System.out.println(cx.toString()));

                    System.out.println(messageResponse);

                    break;
                }
                case 16: {
                    List<GroupResponse> groupResponses = client.listGroups(System.getProperty("whatsapp.session.default.uuid"), cx -> System.out.println(cx.toString()));
                    groupResponses.forEach(cy -> System.out.println(cy.toString()));

                    break;
                }
                case 17: {
                    String gid;

                    System.out.println("Enter Group ID:  ");
                    gid = sc.next().trim();

                    GroupInfo groupInfo = client.infoGroup(System.getProperty("whatsapp.session.default.uuid"), gid, cx -> System.out.println(cx.toString()));

                    System.out.println(groupInfo);

                    break;
                }
                case 18: {
                    String gid;

                    System.out.println("Enter Group ID:  ");
                    gid = sc.next().trim();

                    Message message = new Message();
                    message.setGChatId(gid);
                    message.setText("Hi... I love you");

                    MessageResponse response = client.sendTextSession(System.getProperty("whatsapp.session.default.uuid"), message, cx -> System.out.println(cx.toString()));

                    System.out.println(response);

                    break;
                }
                default:
                    System.out.println("Invalid Option");
            }
        }
    }
}
