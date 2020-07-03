import com.dto.BaseUserProfileDto;
import com.dto.UserProfileDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.helper.ValidateHelper;
import com.service.DatabaseService;
import spark.servlet.SparkApplication;

import static spark.Spark.get;
import static spark.Spark.post;

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
	}

	private static String processException(Exception ex) {
		return ex.getMessage() == null ? "Error" : ex.getMessage();
	}
}
