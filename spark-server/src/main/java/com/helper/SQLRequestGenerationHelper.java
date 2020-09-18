package com.helper;

import com.dictionary.Sex;
import com.dto.UserFilterDto;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLRequestGenerationHelper {

	public static String generateUserSearchRequest(UserFilterDto filter, String login) {
		StringBuilder sb = new StringBuilder("with CTE as (\n" +
				"    select user_profile_id, location_1, location_2 from \"spark_db\".t_user_profile where login='" + login + "'\n" +
				"    )\n" +
				"   , CTE2 as (\n" +
				"    select\n" +
				"         user_profile_id\n" +
				"         ,wsg84_get_distance( (select CTE.location_1 from CTE limit 1), (select CTE.location_2 from CTE limit 1), location_1, location_2) as distance\n" +
				"    from spark_db.t_user_profile\n" +
				")\n" +
				"select *\n" +
				"     ,(select id_image from \"spark_db\".t_image  where u.user_profile_id=user_id and is_main=true limit 1) as photo\n" +
				"     ,(select  count(*) from spark_db.t_users_unity where u.user_profile_id=user2_id or (u.user_profile_id=user1_id and confirmed=true)) as has_like\n" +
				"     ,(select  count(*) from spark_db.t_complaint where u.user_profile_id=to_user) as has_dislike\n" +
				"     ,CTE2.distance\n" +
				"from \"spark_db\".t_user_profile u\n" +
				"join CTE2 on CTE2.user_profile_id=u.user_profile_id");
		boolean hasCondition = false;
		if (login != null) {
			hasCondition = true;
			sb.append(" where login<>?");
		}
		if (filter != null && filter.hasFields()) {
			if (filter.getDistance() != null) {
				sb.append(hasCondition ? " and" : " where").
						append(" CTE2.distance<=?");
				hasCondition = true;
			}
			if (filter.getSexPreferences() != null) {
				sb.append(hasCondition ? " and" : " where").append(" (sex_preferences=? or sex_preferences is null)");
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
			}
		}
		sb.append(" order by rating desc\n" +
				",(select count(*) from spark_db.t_tag t1, spark_db.t_tag t2\n" +
				"    where t1.user_id=(select user_profile_id from CTE) and t2.user_id=u.user_profile_id and t1.name=t2.name) desc\n" +
				",CTE2.distance");
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
