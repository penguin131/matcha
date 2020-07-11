import com.chat.Chat;
import com.chat.ChatWebSocketHandler;
import com.dto.BaseUserProfileDto;
import com.dto.CredentialsDto;
import com.dto.MessageDto;
import com.dto.UserProfileDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.helper.LoggerConfig;
import com.helper.ValidateHelper;
import com.mail.MailService;
import com.security.JWTHelper;
import com.security.SecurityHelper;
import com.service.DatabaseService;
import io.jsonwebtoken.Claims;
import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import spark.Response;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class MajorEndpoint {

	private final static Logger logger = Logger.getLogger(MajorEndpoint.class);

	private static long TTL = 1000000000;
	public static void main(String[] args) {
		port(8080);
		staticFiles.location("/public");
		webSocket("/chat", ChatWebSocketHandler.class);
		try {
			LoggerConfig.configureLogger();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		get("/protected/hello", (req, res) -> "Hello world!");
		get("/protected/getAllUsers", (req, res) -> {
			try {
				return mapper.writeValueAsString(DatabaseService.getAllUsers());
			} catch (Exception ex) {
				return processException(ex);
			}
		});

		post("/createUserProfile", (req, res) -> {
			try {
				BaseUserProfileDto user = mapper.readValue(req.body(), BaseUserProfileDto.class);
				ValidateHelper.validateBaseUserProfile(user);
				String hash = SecurityHelper.generateHash();
				DatabaseService.createUserProfile(user, hash);
				MailService.sendConfirmationEmail(user.getEmail(), hash);
			} catch (Exception ex) {
				return processException(ex);
			}
			return "";
		});

		get("/protected/getUserProfileForLogin/:login", (req, res) -> {
			try {
				return mapper.writeValueAsString(DatabaseService.getUserProfileForLogin(req.params(":login")));
			} catch (Exception ex) {
				return processException(ex);
			}
		});

		get("/protected/getAllFriends", (req, res) -> {
			try {
				String login = JWTHelper.getUserName(req.headers("Authorization"));
				return mapper.writeValueAsString(DatabaseService.getAllFriendsForLogin(login));
			} catch (Exception ex) {
				return processException(ex);
			}
		});

		get("/protected/setLike/:to", (req, res) -> {
			try {
				String from = JWTHelper.getUserName(req.headers("Authorization"));
				String to = req.params(":to");
				DatabaseService.setLike(from, to);
				return "OK";
			} catch (Exception ex) {
				return processException(ex);
			}
		});

		get("/protected/deleteUserProfileForLogin/:login", (req, res) -> {
			try {
				DatabaseService.deleteUserProfileForLogin(req.params(":login"));
				return "OK";
			} catch (Exception ex) {
				return processException(ex);
			}
		});

		post("/protected/updateUserProfile", (req, res) -> {
			try {
				UserProfileDto user = mapper.readValue(req.body(), UserProfileDto.class);
				ValidateHelper.validateBaseUserProfile(user);
				DatabaseService.updateUserProfile(user);
			} catch (Exception ex) {
				return processException(ex);
			}
			return "";
		});

		get("/sendMail/:email/:hash", (req, res) -> {
			try {
				MailService.sendConfirmationEmail(req.params(":email"), req.params(":hash"));
			} catch (Exception ex) {
				return processException(ex);
			}
			return "OK";
		});

		get("/verification/:hash", (req, res) -> {
			DatabaseService.confirmUserForToken(req.params(":hash"));
			return "Your account is verified!";
		});

		get("protected/getChatHistory/:user", (req, res) -> {
			String user = JWTHelper.getUserName(req.headers("Authorization"));
			List<MessageDto> messages = DatabaseService.getChatHistory(req.params(":user"), user);
			return mapper.writeValueAsString(messages);
		});

		get("/kek", (req, res) -> {
			for (Map.Entry<Session, String> entry : Chat.activeUserMap.entrySet()) {
				entry.getKey().getRemote().sendString("OLOLO");
			}
			return "";
		});

		/**
		 * Security
		 */
		post("/getToken", (req, res) -> {
			try {
				CredentialsDto credentials = mapper.readValue(req.body(), CredentialsDto.class);
				String login = credentials.getLogin();
				String password = credentials.getPassword();
				if (DatabaseService.checkPassword(login, password)) {
					return JWTHelper.createJWT(login, "securityService", "security", TTL);
				} else {
					return "Invalid login/password";
				}
			} catch (Exception ex) {
				return processException(ex);
			}
		});

		before((request, response) -> logger.info("==> Request start: " + request.url()));

		afterAfter((request, response) -> logger.info("<== Request end: " + request.url()));

		before("/protected/*", (request, response) -> {
			Claims claims = null;
			long currentTime = 0;
			try {
				addHeaders(response);
				String JWTToken = request.headers("Authorization");
				claims = JWTHelper.decodeJWT(JWTToken);
				currentTime = System.currentTimeMillis();
			} catch (Exception ex) {
				logger.info("before() Exception: " + ex.getMessage());
				halt(403, "403 Forbidden");
			}
			if (currentTime > claims.getExpiration().getTime()) {
				logger.info("Token timed out.");
				halt(403, "403 Forbidden");
			}
		});
		init();
	}

	private static void	addHeaders(Response res) {
		res.header("Access-Control-Allow-Origin","*");
		res.header("Access-Control-Allow-Methods","GET, PUT, POST, DELETE");
		res.header("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept");
	}

	private static String processException(Exception ex) {
		return ex.getMessage() == null ? "Error" : ex.getMessage();
	}
}
