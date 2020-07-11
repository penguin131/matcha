package com.chat;

import com.dictionary.MessageType;
import com.dto.MessageDto;
import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Chat {

    // this map is shared between sessions and threads, so it needs to be thread-safe (http://stackoverflow.com/a/2688817)
    public static Map<Session, String> activeUserMap = new ConcurrentHashMap<>();
    private final static Logger logger = Logger.getLogger(Chat.class);

    public static void sendMessage(String sender, MessageDto message) throws IOException {
        if (MessageType.CHAT_MESSAGE.equals(message.getType())) {
            logger.info("sendMessage(): Chat message");
            for (Map.Entry<Session, String> entry : activeUserMap.entrySet()) {
                if (entry.getValue().equals(sender) || entry.getValue().equals(message.getTo())) {
                     entry.getKey().getRemote().sendString(String.valueOf(new JSONObject()
                        .put("type", message.getType().ordinal())
                        .put("msgText", message.getMsgText())
                        .put("from", message.getFrom())
                        .put("to", message.getTo())
                        .put("date", message.getDate())));
                }
            }
        } else if (MessageType.NOTIFICATION.equals(message.getType())) {

        }
    }
}
