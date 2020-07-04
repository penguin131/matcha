import com.dto.BaseUserProfileDto;
import com.dto.UserProfileDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.helper.ValidateHelper;
import com.security.JWTHelper;
import com.service.DatabaseService;
import spark.servlet.SparkApplication;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.before;

public class MajorEndpoint implements SparkApplication {

	public static void main(String[] args) {
		new MajorEndpoint().init();
	}

	@Override
	public void init() {
		ObjectMapper mapper = new ObjectMapper();
		get("/hello", (req, res) -> "Hello world!");

		get("/getAllUsers", (req, res) -> {
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

		get("/getUserProfileForLogin/:login", (req, res) -> {
			try {
				return mapper.writeValueAsString(DatabaseService.getUserProfileForLogin(req.params(":login")));
			} catch (Exception ex) {
				return processException(ex);
			}
		});

		post("/updateUserProfile", (req, res) -> {
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
			String login = req.queryParams("login");
			String password = req.queryParams("password");
			try {
				if (DatabaseService.checkPassword(login, password)) {
					return JWTHelper.createJWT("1", login, "1234567890", 1000);
				} else {
					res.status(403);
					return "403 Forbidden";
				}
			} catch (Exception ex) {
				return processException(ex);
			}
		});

//		before((request, response) -> {
//			//todo decode jwt and check his time, recreate the token if necessary
//		});
	}

	private static String processException(Exception ex) {
		return ex.getMessage() == null ? "Error" : ex.getMessage();
	}
}
