package com.chat;

import com.dictionary.MessageType;
import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static j2html.TagCreator.*;

public class Chat {

    // this map is shared between sessions and threads, so it needs to be thread-safe (http://stackoverflow.com/a/2688817)
    static Map<Session, String> activeUserMap = new ConcurrentHashMap<>();
    private final static Logger logger = Logger.getLogger(Chat.class);

    //Sends a message from one user to all users, along with a list of current usernames
    public static void broadcastMessage(String sender, String message) {
        activeUserMap.keySet().stream().filter(Session::isOpen).forEach(session -> {
            try {
                session.getRemote().sendString(String.valueOf(new JSONObject()
                    .put("userMessage", createHtmlMessageFromSender(sender, message))
                    .put("userlist", activeUserMap.values())
                ));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void sendMessage(String sender, Message message) throws IOException {
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

    //Builds a HTML element with a sender-name, a message, and a timestamp,
    private static String createHtmlMessageFromSender(String sender, String message) {
        return article(
            b(sender + " says:"),
            span(attrs(".timestamp"), new SimpleDateFormat("HH:mm:ss").format(new Date())),
            p(message)
        ).render();
    }
}
