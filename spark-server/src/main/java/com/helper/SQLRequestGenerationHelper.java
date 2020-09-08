package com.helper;

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
		if (filter != null && filter.hasFields()) {
			sb.append(" where login<>'").append(login).append("'");
			if (filter.getDistance() != null) {
				sb.append(" and wsg84_check_distance(" +
						" (select CTE.location_1 from CTE limit 1)," +
						" (select CTE.location_2 from CTE limit 1)," +
						" location_1,location_2,?)=true");
			}
			if (filter.getSexPreferences() != null) sb.append(" and sex_preferences=?");
			if (filter.getSex() != null) sb.append(" and sex=?");
			if (filter.getRating() != null) sb.append(" and rating=?");
		}
		sb.append(" order by rating desc");
		return sb.toString();
	}

	public static void addValuesToPreparedStatement(PreparedStatement statement, UserFilterDto filter) throws SQLException {
		if (filter == null) return;
		int counter = 1;
		if (filter.getDistance() != null) statement.setInt(counter++, filter.getDistance());
		if (filter.getSexPreferences() != null) statement.setString(counter++, filter.getSexPreferences());
		if (filter.getSex() != null) statement.setString(counter++, filter.getSex());
		if (filter.getRating() != null) statement.setInt(counter, filter.getRating());
	}
}
