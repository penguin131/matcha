package com;

import com.dictionary.MessageType;
import com.dto.MessageDto;
import com.exceptions.AccessDeniedException;
import com.exceptions.ValidateException;
import com.helper.Config;
import com.helper.ServiceHelper;
import com.service.LogicService;
import com.sockets.WebSocketHandler;
import com.sockets.WebSockets;
import io.jsonwebtoken.Claims;
import org.apache.log4j.Logger;
import spark.Filter;
import spark.Spark;

import javax.mail.internet.AddressException;
import java.util.HashMap;
import java.util.Map;

import static com.security.JWTHelper.decodeJWT;
import static com.security.JWTHelper.getUserNameFromToken;
import static spark.Spark.*;

public class Endpoint {
	private static final Map<String, String> corsHeaders = new HashMap<>();
	static {
		corsHeaders.put("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
		corsHeaders.put("Access-Control-Allow-Origin", "*");
		corsHeaders.put("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,");
		corsHeaders.put("Access-Control-Expose-Headers", "date,access-control-allow-origin,access-control-allow-methods,access-control-allow-headers,content-type,connection,server,x-final-url");
	}

	private static void applyFilters() {
		Filter filter = (request, response) -> corsHeaders.forEach(response::header);
		Spark.after(filter);
	}

	public static void main(String[] args) {
		//Инициализация стартовых обьектов
		Logger logger = Logger.getLogger(Endpoint.class);
		LogicService logicService = ServiceHelper.getLogicService();
		port(8080);
		staticFiles.location("/public");
		webSocket("/chat", WebSocketHandler.class);
		Config.configureLogger();

		applyFilters();
		//REST
		get("/protected/hello", (req, res) -> "Hello world!");
		get("/protected/getAllUsers", (req, res) -> {
			String login = getUserNameFromToken(req.headers("Authorization"));
			return logicService.getAllUsers(login);
		});

		post("/createUserProfile", (req, res) -> {
			try {
				logicService.createUserProfile(req);
			} catch (ValidateException ex) {
				return ex.getMessage();
			}
			return "";
		});

		get("/protected/getUserProfileForLogin/:login", (req, res) -> {
			String login = getUserNameFromToken(req.headers("Authorization"));
			return logicService.getUserProfileForLogin(req.params(":login"), login);
		});

		get("/protected/getAllFriends", (req, res) -> {
			String login = getUserNameFromToken(req.headers("Authorization"));
			return logicService.getAllFriendsForLogin(login);
		});

		get("/protected/getAllLikedUsers", (req, res) -> {
			String login = getUserNameFromToken(req.headers("Authorization"));
			return logicService.getAllLikedUsers(login);
		});

		get("/protected/getAllLookedUsers", (req, res) -> {
			String login = getUserNameFromToken(req.headers("Authorization"));
			return logicService.getAllLookedUsers(login);
		});

		get("/protected/getUsersWithFilter", (req, res) -> {
			String login = getUserNameFromToken(req.headers("Authorization"));
			return logicService.getUsersWithFilter(req.body(), login);
		});

		get("/protected/setLike/:to", (req, res) -> {
			try {
				String from = getUserNameFromToken(req.headers("Authorization"));
				String to = req.params(":to");
				logicService.setLike(from, to);
				return "";
			} catch (ValidateException ex) {
				return ex.getMessage();
			}
		});

		get("/protected/setComplaint/:to", (req, res) -> {
			String from = getUserNameFromToken(req.headers("Authorization"));
			String to = req.params(":to");
			logicService.setComplaint(from, to);
			return "";
		});

		get("/protected/deleteUserProfileForLogin/:login", (req, res) -> {
			logicService.deleteUserProfileForLogin(req.params(":login"));
			return "";
		});

		post("/protected/updateUserProfile", (req, res) -> {
			try {
				String from = getUserNameFromToken(req.headers("Authorization"));
				logicService.updateUserProfile(req, from);
				return "";
			} catch (AccessDeniedException ex) {
				halt(403, "403 Forbidden");
				return "";
			} catch (ValidateException | AddressException ex) {
				return ex.getMessage();
			}
		});

		post("/protected/updateCoordinates", (req, res) -> {
			String login = getUserNameFromToken(req.headers("Authorization"));
			logicService.updateUserCoordinates(req.body(), login);
			return "";
		});

		//Подтверждение почты
		get("/verification/:hash", (req, res) -> logicService.confirmUserForToken(req.params(":hash")));

		//Смена почты
		get("/changeMail/:hash", (req, res) -> logicService.updateUserMail(req.params(":hash")));

		get("/protected/getChatHistory/:user", (req, res) -> {
			String user = getUserNameFromToken(req.headers("Authorization"));
			return logicService.getChatHistory(user, req.params(":user"));
		});

		//Security
		post("/getToken", (req, res) -> {
			try {
				return logicService.getToken(req);
			} catch (AccessDeniedException ex) {
				res.status(403);
				return ex.getMessage();
			}
		});

		post("/protected/downloadImage", ((req, res) -> {
			String login = getUserNameFromToken(req.headers("Authorization"));
			logicService.downloadImage(login, req.bodyAsBytes());
			return "";
		}));

		get("/protected/deleteImage/:id", ((req, res) -> {
			try {
				String login = getUserNameFromToken(req.headers("Authorization"));
				logicService.deleteImage(login, req.params(":id"));
			} catch (AccessDeniedException ex) {
				res.status(403);
			}
			return "";
		}));

		get("/image/:id", ((request, response) -> {
			response.type("image/jpeg");
			return logicService.getImage(request.params(":id"));
		}));

		get("/protected/setAvatar/:imageId", (request, response) -> {
			String login = getUserNameFromToken(request.headers("Authorization"));
			logicService.setMainImage(request.params(":imageId"), login);
			return "";
		});

		get("/protected/getUserPhotos/:login", (request, response) -> logicService.getUserPhotos(request.params(":login")));

		post("/saveMessage", (req, res) -> {
			logicService.saveChatMessage(req.body());
			return "";
		});

		post("/protected/getNextUser", ((request, response) -> {
			String login = getUserNameFromToken(request.headers("Authorization"));
			return logicService.getNextUser(login, request.body());
		}));

		post("/notification", ((request, response) -> {
			MessageDto notification = new MessageDto();
			notification.setTo("bfalmer");
			notification.setType(MessageType.NOTIFICATION.getName());
			notification.setMsgText("Test notification");
			WebSockets.sendMessage(null, notification);
			return "";
		}));

		post("/testChat", ((request, response) -> {
			MessageDto notification = new MessageDto();
			notification.setTo("bfalmer");
			notification.setFrom("smight");
			notification.setType(MessageType.CHAT_MESSAGE.getName());
			notification.setMsgText("Test chat message");
			WebSockets.sendMessage(null, notification);
			return "";
		}));

		get("/forbidden", (request, response) -> "");

		before((request, response) -> {
			if ("OPTIONS".equals(request.requestMethod()))
				return;
			logger.info("==> Request start: " + request.url());
		});

		afterAfter((request, response) -> {
			if ("OPTIONS".equals(request.requestMethod()))
				return;
			logger.info("<== Request end: " + request.url());
		});

		before("/protected/*", (request, response) -> {
			if ("OPTIONS".equals(request.requestMethod()))
				return;
			Claims claims = null;
			long currentTime = 0;
			try {
				String JWTToken = request.headers("Authorization");
				claims = decodeJWT(JWTToken);
				currentTime = System.currentTimeMillis();
			} catch (Exception ex) {
				logger.info("before() Exception: " + ex.getMessage());
				response.redirect("/forbidden", 403);
			}
			if (claims == null || currentTime > claims.getExpiration().getTime()) {
				logger.info("Token timed out.");
				response.redirect("/forbidden", 403);
			}
		});
		init();
	}
}
