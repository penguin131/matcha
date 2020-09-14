package com.helper;

import com.dictionary.Sex;
import com.dto.UserFilterDto;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLRequestGenerationHelper {

	public static String generateUserSearchRequest(UserFilterDto filter, String login) {
		StringBuilder sb = new StringBuilder(
				"with CTE as (\n" +
				"    select location_1, location_2 from \"spark_db\".t_user_profile where login='" + login + "'" +
				" )" +
				" select *," +
				" (select id_image from \"spark_db\".t_image " +
				" where user_profile_id=user_id and is_main=true limit 1) as photo " +
				" from \"spark_db\".t_user_profile");
		boolean hasCondition = false;
		if (login != null) {
			hasCondition = true;
			sb.append(" where login<>?");
		}
		if (filter != null && filter.hasFields()) {
			if (filter.getDistance() != null) {
				sb.append(hasCondition ? " and" : " where").
						append(" wsg84_check_distance(" +
						" (select CTE.location_1 from CTE limit 1)," +
						" (select CTE.location_2 from CTE limit 1)," +
						" location_1,location_2,?)=true");
				hasCondition = true;
			}
			if (filter.getSexPreferences() != null) {
				sb.append(hasCondition ? " and" : " where").append(" sex_preferences=?");
				hasCondition = true;
			}
			if (filter.getSex() != null) {
				sb.append(hasCondition ? " and" : " where").append(" sex=?");
				hasCondition = true;
			}
			if (filter.getRating() != null) {
				sb.append(hasCondition ? " and" : " where").append(" rating>=?");
				hasCondition = true;
			}
			if (filter.getAgeMax() != null) {
				sb.append(hasCondition ? " and" : " where").append(" age<=?");
				hasCondition = true;
			}
			if (filter.getAgeMin() != null) {
				sb.append(hasCondition ? " and" : " where").append(" age>=?");
				hasCondition = true;
			}
		}
		sb.append(" order by rating desc");
		return sb.toString();
	}

	public static void addValuesToPreparedStatement(PreparedStatement statement, UserFilterDto filter, String login) throws SQLException {
		int counter = 1;
		if (login != null) statement.setString(counter++, login);
		if (filter == null) return;
		if (filter.getDistance() != null) statement.setInt(counter++, filter.getDistance());
		if (filter.getSexPreferences() != null) statement.setInt(counter++, Sex.convertStringToCode(filter.getSexPreferences()));
		if (filter.getSex() != null) statement.setInt(counter++, Sex.convertStringToCode(filter.getSex()));
		if (filter.getRating() != null) statement.setInt(counter++, filter.getRating());
		if (filter.getAgeMax() != null) statement.setInt(counter++, filter.getAgeMax());
		if (filter.getAgeMin() != null) statement.setInt(counter, filter.getAgeMin());
	}
}