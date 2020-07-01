import com.dto.UserProfileDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.helper.DatabaseConfig;
import spark.servlet.SparkApplication;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static spark.Spark.get;

public class MajorEndpoint implements SparkApplication {
	public static void main(String[] args) {
		new MajorEndpoint().init();
	}

	@Override
	public void init() {
		DatabaseConfig.DatabaseProperties props = DatabaseConfig.getDatabaseProperties();
		try {
			List<UserProfileDto> userProfileList = new ArrayList<>();
			Connection connection = DriverManager.getConnection(props.getUrl(), props.getProperties());
			PreparedStatement preparedStatement = connection.prepareStatement(
					"select * from my_db.t_user_profile");
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				UserProfileDto userProfile = new UserProfileDto(
						rs.getString("first_name"),
						rs.getString("last_name"),
						rs.getString("login"),
						rs.getString("password"),
						rs.getInt("sex"),
						rs.getInt("sex_preferences"),
						rs.getString("biography"),
						rs.getString("email"),
						rs.getInt("confirmed"));
				userProfileList.add(userProfile);
				ObjectMapper mapper = new ObjectMapper();
				String reslt = mapper.writeValueAsString(userProfileList);

				get("/hello", (req, res) -> reslt);
			}

		} catch (Exception e) {
			get("/hello", (req, res) -> e.getMessage());
		}

	}
}
