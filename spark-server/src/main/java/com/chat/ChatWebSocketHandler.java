package com.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.JWTHelper;
import io.jsonwebtoken.Claims;
import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import spark.utils.StringUtils;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.List;

@WebSocket
public class ChatWebSocketHandler {
    private final static Logger logger = Logger.getLogger(ChatWebSocketHandler.class);
    private static ObjectMapper mapper = new ObjectMapper();

    @OnWebSocketConnect
    public void onConnect(Session userSession) throws Exception {
        logger.info("onConnect()");
        String token = userSession.getUpgradeRequest().getParameterMap().get("token").get(0);
        String username = JWTHelper.getUserName(token);
        if (StringUtils.isEmpty(username)) {
            logger.info("Empty username");
            throw new Exception("Empty username");
        }
        Chat.activeUserMap.put(userSession, username);
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        Chat.activeUserMap.remove(user);
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) throws IOException {
        Message message1 = mapper.readValue(message, Message.class);
        Chat.sendMessage(Chat.activeUserMap.get(user), message1);
        //todo save to database
    }
}
