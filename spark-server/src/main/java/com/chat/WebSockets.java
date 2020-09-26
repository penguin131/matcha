package com.chat;

import com.dto.MessageDto;
import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebSockets {

    public static Map<Session, String> activeUserMap = new ConcurrentHashMap<>();
    private final static Logger logger = Logger.getLogger(WebSockets.class);

    public static void sendMessage(String sender, MessageDto message) throws IOException {
        logger.info("sendMessage(): ");
        for (Map.Entry<Session, String> entry : activeUserMap.entrySet()) {
            if (entry.getValue().equals(sender) || entry.getValue().equals(message.getTo())) {
                entry.getKey().getRemote().sendString(String.valueOf(new JSONObject()
                        .put("type", message.getType())
                        .put("msgText", message.getMsgText())
                        .put("from", message.getFrom())
                        .put("to", message.getTo())
                        .put("date", message.getDate())));
            }
        }
    }
}
