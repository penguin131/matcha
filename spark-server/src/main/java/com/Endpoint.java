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
	private static Logger logger = Logger.getLogger(Endpoint.class);
	private static LogicService logicService = ServiceHelper.getLogicService();
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

	private static void initStartObjects() {
		port(8080);
		staticFiles.location("/public");
		webSocket("/chat", WebSocketHandler.class);
		Config.configureLogger();
		applyFilters();
	}


	public static void main(String[] args) {
		initStartObjects();

		get("/protected/getAllCoordinates", (req, res) -> {
			if (res.status() == 403){
				return "";
			}
			return logicService.getAllCoordinates();
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
			if (res.status() == 403){
				return "";
			}
			String login = getUserNameFromToken(req.headers("Authorization"));
			return logicService.getUserProfileForLogin(req.params(":login"), login);
		});

		get("/protected/getAllFriends", (req, res) -> {
			if (res.status() == 403){
				return "";
			}
			String login = getUserNameFromToken(req.headers("Authorization"));
			return logicService.getAllFriendsForLogin(login);
		});

		get("/protected/getAllLikedUsers", (req, res) -> {
			if (res.status() == 403){
				return "";
			}
			String login = getUserNameFromToken(req.headers("Authorization"));
			return logicService.getAllLikedUsers(login);
		});

		get("/protected/getAllLookedUsers", (req, res) -> {
			if (res.status() == 403){
				return "";
			}
			String login = getUserNameFromToken(req.headers("Authorization"));
			return logicService.getAllLookedUsers(login);
		});

		get("/protected/getUsersWithFilter", (req, res) -> {
			if (res.status() == 403){
				return "";
			}
			String login = getUserNameFromToken(req.headers("Authorization"));
			return logicService.getUsersWithFilter(req.body(), login);
		});

		get("/protected/setLike/:to", (req, res) -> {
			try {
				if (res.status() == 403){
					return "";
				}
				String from = getUserNameFromToken(req.headers("Authorization"));
				String to = req.params(":to");
				logicService.setLike(from, to);
				return "";
			} catch (ValidateException ex) {
				return ex.getMessage();
			}
		});

		get("/protected/setComplaint/:to", (req, res) -> {
			if (res.status() == 403){
				return "";
			}
			String from = getUserNameFromToken(req.headers("Authorization"));
			String to = req.params(":to");
			logicService.setComplaint(from, to);
			return "";
		});

		get("/protected/complaintAboutFake/:to", (req, res) -> {
			if (res.status() == 403){
				return "";
			}
			String from = getUserNameFromToken(req.headers("Authorization"));
			String to = req.params(":to");
			logicService.setComplaintAboutFake(from, to);
			return "";
		});

		get("/protected/deleteUserProfileForLogin/:login", (req, res) -> {
			if (res.status() == 403){
				return "";
			}
			logicService.deleteUserProfileForLogin(req.params(":login"));
			return "";
		});

		post("/protected/updateUserProfile", (req, res) -> {
			if (res.status() == 403){
				return "";
			}
			try {
				if (res.status() == 403){
					return "";
				}
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
			if (res.status() == 403){
				return "";
			}
			String login = getUserNameFromToken(req.headers("Authorization"));
			logicService.updateUserCoordinates(req.body(), login);
			return "";
		});

		//Подтверждение почты
		get("/verification/:hash", (req, res) -> logicService.confirmUserForToken(req.params(":hash")));

		//Смена почты
		get("/changeMail/:hash", (req, res) -> logicService.updateUserMail(req.params(":hash")));

		get("/protected/getChatHistory/:user", (req, res) -> {
			if (res.status() == 403){
				return "";
			}
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
			if (res.status() == 403){
				return "";
			}
			String login = getUserNameFromToken(req.headers("Authorization"));
			logicService.downloadImage(login, req.bodyAsBytes());
			return "";
		}));

		get("/protected/deleteImage/:id", ((req, res) -> {
			try {
				if (res.status() == 403){
					return "";
				}
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
			if (response.status() == 403){
				return "";
			}
			String login = getUserNameFromToken(request.headers("Authorization"));
			logicService.setMainImage(request.params(":imageId"), login);
			return "";
		});

		get("/protected/getUserPhotos/:login", (request, response) -> {
			if (response.status() == 403){
				return "";
			}
			return logicService.getUserPhotos(request.params(":login"));
		});

		post("/protected/getNextUser", ((request, response) -> {
			if (response.status() == 403){
				return "";
			}
			String login = getUserNameFromToken(request.headers("Authorization"));
			return logicService.getNextUser(login, request.body());
		}));

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
			Claims claims;
			long currentTime;
			try {
				String JWTToken = request.headers("Authorization");
				claims = decodeJWT(JWTToken);
				currentTime = System.currentTimeMillis();
			} catch (Exception ex) {
				logger.info("before() Exception: " + ex.getMessage());
				response.status(403);
				return;
			}
			if (currentTime > claims.getExpiration().getTime()) {
				logger.info("Token timed out.");
				response.status(403);
			}
		});
		init();
	}
}
