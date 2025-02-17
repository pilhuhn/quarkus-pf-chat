package org.patternfly.chat.bot;

import io.quarkus.websockets.next.*;
import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import java.io.StringReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@WebSocket(path = "/websocket")
public class ChatResourceWS {


    private final MyChatService bot;

    public ChatResourceWS(MyChatService bot) {
        this.bot = bot;
    }

    @OnOpen
    // TODO skip for now as the frontend produces a garbled item if we send this
    public Map<String,String> onOpen(WebSocketConnection conn) {
        System.out.println("ws open ");
        return null;
//        Map<String,String> out = new HashMap<>();
//        out.put("event","message");
//        out.put("message", "Hello, how can I help you?");
//        return out;
    }

    @OnClose
    public void onClose() {
        System.out.println("WS closed");
    }

    @OnTextMessage
    public Map<String,String> onMessage(WebSocketConnection conn, String query) {
        System.out.println("WS: got : " + query);
        String eventType;
        JsonObject jsonObject;

        Map<String,String> out = new HashMap<>();
        out.put("event","message");


        try {
            jsonObject = Json.createReader(new StringReader(query)).readObject();
            eventType = jsonObject.get("event").toString();
        } catch (Exception e) {
            out.put("data","!E " + e.getMessage());
            return out;
        }
        if ("\"subscribe\"".equals(eventType)) {
            out.put("data", "Hello, nice to meet you");
            return out;
        }

        if ("unsubscribe".equals(eventType)) {
            System.out.println(conn.close());
            return null;
        }

        String msg = jsonObject.get("data").toString();
        msg = msg.strip();

        String topic = msg != null && !msg.isEmpty() ? msg : "PatternFly";

        String result = bot.patternFlyInfo(topic);
        // String result = "this is a demo result for . _" +topic + "_ " + new Date().toString();

        out.put("data",result);

        return out;
    }

}
