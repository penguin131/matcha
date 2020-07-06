import com.dto.BaseUserProfileDto;
import com.dto.CredentialsDto;
import com.dto.UserProfileDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.helper.LoggerConfig;
import com.helper.ValidateHelper;
import com.mail.MailService;
import com.security.JWTHelper;
import com.service.DatabaseService;
import io.jsonwebtoken.Claims;
import org.apache.log4j.Logger;
import spark.Response;
import spark.servlet.SparkApplication;

import java.io.IOException;

import static spark.Spark.*;

public class MajorEndpoint implements SparkApplication {

	private final static Logger logger = Logger.getLogger(MajorEndpoint.class);

	private static long TTL = 1000000000;
	public static void main(String[] args) {
		new MajorEndpoint().init();
	}

	@Override
	public void init() {
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
				//MailService.sendConfirmationEmail(user.getEmail());
				DatabaseService.createUserProfile(user);
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

		get("/sendMail/:email", (req, res) -> {
			try {
				MailService.sendConfirmationEmail(req.params(":email"));
			} catch (Exception ex) {
				return processException(ex);
			}
			return "OK";
		});

		get("/verification/:hash", (req, res) -> {
			DatabaseService.confirmUserForToken(req.params(":hash"));
			return "Your account is verified!";
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

		before((request, response) -> {
			logger.info("==> Request start: " + request.url());
		});

		afterAfter((request, response) -> {
			logger.info("<== Request end: " + request.url());
		});

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
	}

	private static void	addHeaders(Response res) {
		res.header("Access-Control-Allow-Origin","*");
		res.header("Access-Control-Allow-Methods","GET, PUT, POST, DELETE");
		res.header("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept");
	}

	private static String processException(Exception ex) {
//		logger.info("Exception message: " + ex.getMessage());
		return ex.getMessage() == null ? "Error" : ex.getMessage();
//		return ex.getMessage() == null ? "Error. Current directory: " + System.getProperty("user.dir") : ex.getMessage();
	}
}
