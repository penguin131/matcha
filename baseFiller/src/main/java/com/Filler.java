package com;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utils.Config;
import com.utils.Coordinate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.util.Random;

public class Filler {
	private static final String MODE_COORDINATES = "coordinates";
	private static final String MODE_USERS = "users";
	private static ObjectMapper mapper = new ObjectMapper();
	private static Connection connection;

	public static void main(String[] args) throws Exception {
		//Режим апдейта координат
		if (args.length == 1 && args[0].length() == 11 && MODE_COORDINATES.equals(args[0])) {
			Coordinate[] coordinates = mapper.readValue(new File(Config.getCoordinatesFile()), Coordinate[].class);
			if (coordinates.length < 1) throw new Exception("Zero coordinates file!");
			//1 - получаю список айдишников юзеров. 2 - запускаю апдейт на каждый из них
			PreparedStatement preparedStatement = connection.prepareStatement("select user_profile_id from spark_db.t_user_profile");
			ResultSet rs = preparedStatement.executeQuery();
			int index = 0;
			while (rs.next()) {
				PreparedStatement preparedStatement2 = connection.prepareStatement(
						"update spark_db.t_user_profile set location_1=?, location_2=? where user_profile_id=?");
				preparedStatement2.setFloat(1, coordinates[index].getLatitude());
				preparedStatement2.setFloat(2, coordinates[index].getLongitude());
				preparedStatement2.setInt(3, rs.getInt("user_profile_id"));
				preparedStatement2.executeUpdate();
				index++;
				if (index >= coordinates.length)
					index = 0;
			}
		//Режим заполнения таблички с юзерами
		} else if (args.length == 1 && MODE_USERS.equals(args[0])) {
			BufferedReader emailReader = new BufferedReader(new FileReader(Config.getEmailFile()));
			BufferedReader loginReader = new BufferedReader(new FileReader(Config.getLoginFile()));
			Random rand = new Random();
			for (int i = 0; i < 500; i++) {
				PreparedStatement preparedStatement = connection.prepareStatement(
						"insert into \"spark_db\".t_user_profile (login, password, email, sex, confirmed, rating) " +
								"VALUES (?, '123', ?, ?, false, ?)");
				preparedStatement.setString(1, loginReader.readLine());
				preparedStatement.setString(2, emailReader.readLine());
				preparedStatement.setInt(3, rand.nextInt(2));
				preparedStatement.setInt(4, rand.nextInt(41) - 20);
				preparedStatement.execute();
			}
		} else {
			System.out.println("Write program mode: coordinates or users");
		}
	}

	static {
		try {
			connection = DriverManager.getConnection(Config.getUrl(), Config.getConfig());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

