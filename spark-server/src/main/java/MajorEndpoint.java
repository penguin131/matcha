import com.chat.Chat;
import com.chat.ChatWebSocketHandler;
import com.dto.*;
import com.exceptions.AccessDeniedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.helper.Config;
import com.helper.DatabaseServiceHelper;
import com.helper.Password;
import com.helper.ValidateHelper;
import com.images.ImageManager;
import com.images.ImageManagerImpl;
import com.mail.MailService;
import com.security.SecurityHelper;
import com.service.DatabaseService;
import io.jsonwebtoken.Claims;
import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import spark.Response;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static com.security.JWTHelper.*;
import static spark.Spark.*;

public class MajorEndpoint {

	public static void main(String[] args) {
		//Инициализация стартовых обьектов
		Logger logger = Logger.getLogger(MajorEndpoint.class);
		DatabaseService databaseService = DatabaseServiceHelper.getDatabaseService();
		ObjectMapper mapper = new ObjectMapper();
		long TTL = 1000000000;
		ImageManager imageManager = new ImageManagerImpl();
		port(8080);
		staticFiles.location("/public");
		webSocket("/chat", ChatWebSocketHandler.class);
		Config.configureLogger();

		//REST
		get("/protected/hello", (req, res) -> "Hello world!");
		get("/protected/getAllUsers", (req, res) -> {
			try {
				String login = getUserNameFromToken(req.headers("Authorization"));
				return mapper.writeValueAsString(databaseService.getAllUsers(login));
			} catch (Exception ex) {
				return processException(ex);
			}
		});

		post("/createUserProfile", (req, res) -> {
			try {
				BaseUserProfileDto user = mapper.readValue(req.body(), BaseUserProfileDto.class);
				ValidateHelper.validateBaseUserProfile(user);
				user.setPassword(Password.getSaltedHash(user.getPassword()));
				String hash = SecurityHelper.generateHash();
				databaseService.createUserProfile(user, hash);
				MailService.sendConfirmationEmail(user.getEmail(), hash);
			} catch (Exception ex) {
				return processException(ex);
			}
			return "";
		});

		get("/protected/getUserProfileForLogin/:login", (req, res) -> {
			try {
				return mapper.writeValueAsString(databaseService.getUserProfileForLogin(req.params(":login")));
			} catch (Exception ex) {
				return processException(ex);
			}
		});

		get("/protected/getAllFriends", (req, res) -> {
			try {
				String login = getUserNameFromToken(req.headers("Authorization"));
				return mapper.writeValueAsString(databaseService.getAllFriendsForLogin(login));
			} catch (Exception ex) {
				return processException(ex);
			}
		});

		get("/protected/getUsersWithFilter", (req, res) -> {
			try {
				String login = getUserNameFromToken(req.headers("Authorization"));
				UserFilterDto filter = mapper.readValue(req.body(), UserFilterDto.class);
				return mapper.writeValueAsString(databaseService.getUsersWithFilter(filter, login));
			} catch (Exception ex) {
				return processException(ex);
			}
		});

		get("/protected/setLike/:to", (req, res) -> {
			try {
				String from = getUserNameFromToken(req.headers("Authorization"));
				String to = req.params(":to");
				databaseService.setLike(from, to);
				return "OK";
			} catch (Exception ex) {
				return processException(ex);
			}
		});

		get("/protected/setComplaint/:to", (req, res) -> {
			try {
				String from = getUserNameFromToken(req.headers("Authorization"));
				String to = req.params(":to");
				databaseService.setComplaint(from, to);
				return "OK";
			} catch (Exception ex) {
				return processException(ex);
			}
		});

		get("/protected/deleteUserProfileForLogin/:login", (req, res) -> {
			try {
				databaseService.deleteUserProfileForLogin(req.params(":login"));
				return "OK";
			} catch (Exception ex) {
				return processException(ex);
			}
		});

		post("/protected/updateUserProfile", (req, res) -> {
			try {
				logger.info("updateUserProfile body: " + mapper.writeValueAsString(req.body()));
				UserProfileDto user = mapper.readValue(req.body(), UserProfileDto.class);
				ValidateHelper.validateUserProfile(user);
				databaseService.updateUserProfile(user);
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
			databaseService.confirmUserForToken(req.params(":hash"));
			return "Your account is verified!";
		});

		get("/protected/getChatHistory/:user", (req, res) -> {
			String user = getUserNameFromToken(req.headers("Authorization"));
			List<MessageDto> messages = databaseService.getChatHistory(user, req.params(":user"));
			return mapper.writeValueAsString(messages);
		});

		get("/kek", (req, res) -> {
			for (Map.Entry<Session, String> entry : Chat.activeUserMap.entrySet()) {
				logger.info("send OLOLO to socket: " + entry.getValue());
				entry.getKey().getRemote().sendString("OLOLO");
			}
			return "";
		});

		//Security
		post("/getToken", (req, res) -> {
			try {
				CredentialsDto credentials = mapper.readValue(req.body(), CredentialsDto.class);
				String login = credentials.getLogin();
				String password = credentials.getPassword();
				if (databaseService.checkPassword(login, password)) {
					return createJWT(login, "securityService", "security", TTL);
				} else {
					return "Invalid login/password";
				}
			} catch (Exception ex) {
				return processException(ex);
			}
		});

		//Images
		post("/protected/downloadImage", ((req, res) -> {
			try {
				String login = getUserNameFromToken(req.headers("Authorization"));
				imageManager.saveImage(login, req.bodyAsBytes());
				return "OK";
			} catch (IOException ex) {
				return ex.getMessage();
			}
		}));

		get("/protected/deleteImage/:id", ((req, res) -> {
			try {
				String login = getUserNameFromToken(req.headers("Authorization"));
				imageManager.deleteImage(login, req.params(":id"));
				return "OK";
			} catch (IOException ex) {
				return ex.getMessage();
			} catch (AccessDeniedException ex) {
				res.status(404);
				return null;
			}
		}));

		post("/downloadImage", ((req, res) -> {
			try {
				String login = "smight";
				imageManager.saveImage(login, req.bodyAsBytes());
				return "OK";
			} catch (IOException ex) {
				return ex.getMessage();
			}
		}));

		get("/image/:id", ((request, response) -> {
			response.type("image/jpeg");
			return imageManager.getImage(request.params(":id"));
		}));

		get("/protected/setAvatar/:imageId", (request, response) -> {
			try {
				String login = getUserNameFromToken(request.headers("Authorization"));
				databaseService.setMainImage(request.params(":imageId"), login);
				return "OK";
			} catch (SQLException ex) {
				return processException(ex);
			}
		});

		get("/protected/getUserPhotos/:login", (request, response) -> {
			try {
				return mapper.writeValueAsString(databaseService.getUserPhotos(request.params(":login")));
			} catch (SQLException ex) {
				return processException(ex);
			}
		});

		post("/saveMessage", (req, res) -> {
			try {
				MessageDto messageDto = mapper.readValue(req.body(), MessageDto.class);
				databaseService.saveChatMessage(messageDto);
			} catch (Exception ex) {
				return processException(ex);
			}
			return "";
		});

		//Filters
		before((request, response) -> logger.info("==> Request start: " + request.url()));

		afterAfter((request, response) -> logger.info("<== Request end: " + request.url()));

		before("/protected/*", (request, response) -> {
			Claims claims = null;
			long currentTime = 0;
			try {
				addHeaders(response);
				String JWTToken = request.headers("Authorization");
				claims = decodeJWT(JWTToken);
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
		ex.printStackTrace();
		return ex.getMessage() == null ? "Error" : ex.getMessage();
	}
}
