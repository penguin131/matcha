package com.sockets;

import com.dto.MessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.JWTHelper;
import com.service.DatabaseService;
import com.helper.ServiceHelper;
import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import spark.utils.StringUtils;

@WebSocket
public class WebSocketHandler {
    private final static Logger logger = Logger.getLogger(WebSocketHandler.class);
    private static ObjectMapper mapper = new ObjectMapper();
    private static final DatabaseService databaseService = ServiceHelper.getDatabaseService();

    @OnWebSocketConnect
    public void onConnect(Session userSession) throws Exception {
        logger.info("Start connection to web socket");
        userSession.setIdleTimeout(10000000);//примерно 3 часа
        String token = userSession.getUpgradeRequest().getParameterMap().get("token").get(0);
        String username = JWTHelper.getUserNameFromToken(token);
        if (StringUtils.isEmpty(username)) {
            logger.info("Empty username");
            throw new Exception("Empty username");
        }
        logger.info("put user: " + username);
        WebSockets.activeUserMap.put(userSession, username);
        logger.info("User map size: " + WebSockets.activeUserMap.size());
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        logger.info("End connection to web socket");
        WebSockets.activeUserMap.remove(user);
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) throws Exception {
        MessageDto message1 = mapper.readValue(message, MessageDto.class);
        logger.info("Send message: " + mapper.writeValueAsString(message1));
        WebSockets.sendMessage(WebSockets.activeUserMap.get(user), message1);
        databaseService.saveChatMessage(message1);
    }
}
