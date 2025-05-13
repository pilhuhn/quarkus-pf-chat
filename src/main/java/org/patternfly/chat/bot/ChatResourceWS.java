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


    private final MyChatServiceWS bot;

    public ChatResourceWS(MyChatServiceWS bot) {
        this.bot = bot;
    }

    @OnOpen
    // TODO skip for now as the frontend produces a garbled item if we send this
    public String onOpen(WebSocketConnection conn) {
        System.out.println("ws open ");
        return null;
    }

    @OnClose
    public void onClose() {
        System.out.println("WS closed");
    }

    @OnTextMessage
    public Multi<String> onMessage(WebSocketConnection conn, String query) {
        System.out.println("WS: got : " + query);
        JsonObject jsonObject;


        Multi<String> result;

        result= bot.patternFlyInfo(query);

        return result;
    }

}
