package com.service;

import com.TestConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class DatabaseServiceTest {

	private DatabaseService service;
	private Connection connection;

	public DatabaseServiceTest() throws SQLException {
		super();
		this.connection = DriverManager.getConnection(TestConfig.getUrl(), TestConfig.getTestConfig());
		this.service = new DatabaseServiceSQLImpl(connection);
	}

	//Чищу все таблицы перед каждым тестом.
	@Before
	public void before() throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement(
				"truncate table \"spark_db\".t_user_profile, \"spark_db\".t_image, \"spark_db\".t_message, \"spark_db\".t_complaint, \"spark_db\".t_users_unity");
		preparedStatement.executeUpdate();
	}

	@Test
	public void setLikeTest() throws Exception {
		String login1 = "11111";
		String login2 = "22222";
		String login3 = "33333";
		createUserProfile(login1, "1", 1);
		createUserProfile(login2, "2", 0);
		createUserProfile(login3, "3", 0);
		service.setLike(login1, login2);
		assertEquals(service.getUserProfileForLogin(login2, null).getRating(), new Integer(1));
		service.setLike(login1, login2);
		assertEquals(service.getUserProfileForLogin(login2, null).getRating(), new Integer(1));
		service.setLike(login2, login1);
		assertEquals(service.getUserProfileForLogin(login2, null).getRating(), new Integer(1));
		assertEquals(service.getUserProfileForLogin(login1, null).getRating(), new Integer(1));
		service.setLike(login3, login2);
		assertEquals(service.getUserProfileForLogin(login2, null).getRating(), new Integer(2));
	}

	@Test
	public void setComplaintTest() throws SQLException, JsonProcessingException {
		String login1 = "11111";
		String login2 = "22222";
		createUserProfile(login1, "1", 1);
		createUserProfile(login2, "2", 0);
		service.setComplaint(login1, login2);
		assertEquals(service.getUserProfileForLogin(login2, null).getRating(), new Integer(-5));
		service.setComplaint(login1, login2);
		assertEquals(service.getUserProfileForLogin(login2, null).getRating(), new Integer(-5));
		service.setComplaint(login2, login1);
		assertEquals(service.getUserProfileForLogin(login2, null).getRating(), new Integer(-5));
		assertEquals(service.getUserProfileForLogin(login1, null).getRating(), new Integer(-5));
	}

	private void createUserProfile(String login,
								   String password,
								   int sex) {
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(
					"insert into \"spark_db\".t_user_profile (login, password, sex) " +
							"VALUES (?, ?, ?)");
			preparedStatement.setString(1, login);
			preparedStatement.setString(2, password);
			preparedStatement.setInt(3, sex);
			preparedStatement.execute();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
}
