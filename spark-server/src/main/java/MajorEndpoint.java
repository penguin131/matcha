import com.dto.BaseUserProfileDto;
import com.dto.CredentialsDto;
import com.dto.UserProfileDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.helper.ValidateHelper;
import com.security.JWTHelper;
import com.service.DatabaseService;
import io.jsonwebtoken.Claims;
import spark.Response;
import spark.servlet.SparkApplication;

import static spark.Spark.*;

public class MajorEndpoint implements SparkApplication {

	private static long TTL = 1000000000;
	public static void main(String[] args) {
		new MajorEndpoint().init();
	}

	@Override
	public void init() {
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

		/**
		 * Security
		 */
		post("/getToken", (req, res) -> {
			CredentialsDto credentials = mapper.readValue(req.body(), CredentialsDto.class);
			String login = credentials.getLogin();
			String password = credentials.getPassword();
			try {
				if (DatabaseService.checkPassword(login, password)) {
					return JWTHelper.createJWT(login, "securityService", "security", TTL);
				} else {
					res.status(403);
					return "403 Forbidden";
				}
			} catch (Exception ex) {
				return processException(ex) + "Request body: " + req.body();
			}
		});

		before("/protected/*", (request, response) -> {
			try {
				addHeaders(response);
				String JWTToken = request.headers("Authorization");
				Claims claims = JWTHelper.decodeJWT(JWTToken);
				long currentTime = System.currentTimeMillis();
				if (currentTime > claims.getExpiration().getTime()) {
					halt(403, "403 Forbidden");
				}
			} catch (Exception ex) {
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
//		return ex.getMessage() == null ? "Error" : ex.getMessage();
		return ex.getMessage() == null ? "Error. Current directory: " + System.getProperty("user.dir") : ex.getMessage();
	}
}
