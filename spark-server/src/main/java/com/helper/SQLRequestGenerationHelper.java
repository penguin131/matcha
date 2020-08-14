package com.helper;

import com.dto.UserFilterDto;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLRequestGenerationHelper {
	private static boolean isFirst;
	private static synchronized String getDelimiterString() {
		if (isFirst) {
			isFirst = false;
			return "";
		}
		return " and";
	}

	public static synchronized String generateUserSearchRequest(UserFilterDto filter) {
		StringBuilder sb = new StringBuilder(
				"select *," +
						" (select id_image from \"spark-db\".t_image where user_profile_id=user_id and is_main=true limit 1) as photo " +
						" from \"spark-db\".t_user_profile");
		if (filter != null && filter.hasFields()) {
			isFirst = true;
			sb.append(" where");
			//todo расчет дистанции по координатам
//			if (filter.getDistance() != null) sb.append(getDelimiterString()).append(" distance=?");
			if (filter.getSexPreferences() != null) sb.append(getDelimiterString()).append(" sex_preferences=?");
			if (filter.getSex() != null) sb.append(getDelimiterString()).append(" sex=?");
			if (filter.getRating() != null) sb.append(getDelimiterString()).append(" rating=?");
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
