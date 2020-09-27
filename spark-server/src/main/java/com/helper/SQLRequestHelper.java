package com.helper;

import com.dictionary.Sex;
import com.dto.InnerProfileDto;
import com.dto.UserFilterDto;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLRequestHelper {

	private static void addConditions(StringBuilder sb, UserFilterDto filter, String login) {
		sb.append(" where (select count(*) from spark_db.t_image im where im.user_id=u.user_profile_id and im.is_main=true)>0")//есть аватарка
				.append(" and (select count(*) from spark_db.t_complaint \n" +	//нет жалоб
						"    where to_user=(select up.user_profile_id from spark_db.t_user_profile up where up.login='")
				.append(login)
				.append("' limit 1)\n")
				.append("    and from_user=u.user_profile_id)=0")
				.append(" and u.confirmed=true");//Подтвержденные юзеры
		if (login != null) {
			sb.append(" and login<>?");
		}
		if (filter != null && filter.hasFields()) {
			if (filter.getDistance() != null) {
				sb.append(" and CTE2.distance<=?");
			}
			if (filter.getSexPreferences() != null) {
				sb.append(" and (sex_preferences=? or sex_preferences is null)");
			}
			if (filter.getSex() != null) {
				sb.append(" and sex=?");
			}
			if (filter.getRating() != null) {
				sb.append(" and rating>=?");
			}
			if (filter.getAgeMax() != null) {
				sb.append(" and age<=?");
			}
			if (filter.getAgeMin() != null) {
				sb.append(" and age>=?");
			}
		}
		sb.append(" order by rating desc\n" +
				",(select count(*) from spark_db.t_tag t1, spark_db.t_tag t2\n" +
				"    where t1.user_id=(select user_profile_id from CTE) and t2.user_id=u.user_profile_id and t1.name=t2.name) desc\n" +
				",CTE2.distance");
	}

	public static String generateInsertRequest(UserFilterDto filter, String login) {
		StringBuilder sb = new StringBuilder(
				"with CTE as (\n" +
				"    select user_profile_id, latitude, longitude from \"spark_db\".t_user_profile where login=?\n" +
				"    )\n" +
				"   , CTE2 as (\n" +
				"    select\n" +
				"         user_profile_id\n" +
				"         ,wsg84_get_distance( (select CTE.latitude from CTE limit 1), (select CTE.longitude from CTE limit 1), latitude, longitude) as distance\n" +
				"    from spark_db.t_user_profile\n" +
				" )\n" +
				" insert into spark_db.t_search_data (filter_hash, user_id, number) " +
				" select " + (filter == null ? 0 : filter.hashCode()) + " ,u.user_profile_id, row_number() over (" +
				"order by rating desc\n" +
						"       ,(select count(*) from spark_db.t_tag t1, spark_db.t_tag t2\n" +
						"         where t1.user_id=(select user_profile_id from CTE) and t2.user_id=u.user_profile_id and t1.name=t2.name) desc\n" +
						"       ,CTE2.distance" +
				") as number" +
				" from \"spark_db\".t_user_profile u\n" +
				" join CTE2 on CTE2.user_profile_id=u.user_profile_id");
		addConditions(sb, filter, login);
		return sb.toString();
	}

	public static String generateUserSearchRequest(UserFilterDto filter, String login) {
		StringBuilder sb = new StringBuilder("with CTE as (\n" +
				"    select user_profile_id, latitude, longitude from \"spark_db\".t_user_profile where login=?\n" +
				"    )\n" +
				"   , CTE2 as (\n" +
				"    select\n" +
				"         user_profile_id\n" +
				"         ,wsg84_get_distance( (select CTE.latitude from CTE limit 1), (select CTE.longitude from CTE limit 1), latitude, longitude) as distance\n" +
				"    from spark_db.t_user_profile\n" +
				" )\n" +
				" select *\n" +
				"     ,(select id_image from \"spark_db\".t_image  where u.user_profile_id=user_id and is_main=true limit 1) as photo\n" +
				"     ,(select  count(*) from spark_db.t_users_unity where u.user_profile_id=user2_id or (u.user_profile_id=user1_id and confirmed=true)) as has_like\n" +
				"     ,(select  count(*) from spark_db.t_complaint where u.user_profile_id=to_user) as has_dislike\n" +
				"     ,CTE2.distance\n" +
				" ,(select json_agg(name) from spark_db.t_tag where user_id=u.user_profile_id) as tags" +
				" from \"spark_db\".t_user_profile u\n" +
				" join CTE2 on CTE2.user_profile_id=u.user_profile_id");
		addConditions(sb, filter, login);
		return sb.toString();
	}

	public static void addValuesToPreparedStatement(PreparedStatement statement, UserFilterDto filter, String login) throws SQLException {
		statement.setString(1, login);
		int counter = 2;
		if (login != null) statement.setString(counter++, login);
		if (filter == null) return;
		if (filter.getDistance() != null) statement.setInt(counter++, filter.getDistance());
		if (filter.getSexPreferences() != null) statement.setInt(counter++, Sex.convertStringToCode(filter.getSexPreferences()));
		if (filter.getSex() != null) statement.setInt(counter++, Sex.convertStringToCode(filter.getSex()));
		if (filter.getRating() != null) statement.setInt(counter++, filter.getRating());
		if (filter.getAgeMax() != null) statement.setInt(counter++, filter.getAgeMax());
		if (filter.getAgeMin() != null) statement.setInt(counter, filter.getAgeMin());
	}

	public static String generateUpdateUserRequest(InnerProfileDto userProfile) throws IllegalAccessException {
		StringBuilder sb = new StringBuilder("update \"spark_db\".t_user_profile");
		boolean comma = false;

		Field[] fields = userProfile.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			if ("tags".equals(field.getName()) || "newEmail".equals(field.getName())) {
				continue;
			}
			if (field.get(userProfile) != null) {
				if (!comma) {
				 	sb.append(" set ");
				 	comma = true;
				} else {
					sb.append(",");
				}
				sb.append(field.getName()).append("=?");
			}
		}

		sb.append(" where login=?");
		return sb.toString();
	}

	public static void addValuesToPreparedStatement(PreparedStatement statement, InnerProfileDto userProfile, String login)
			throws IllegalAccessException, SQLException {
		Field[] fields = userProfile.getClass().getDeclaredFields();
		int counter = 1;
		for (Field field : fields) {
			field.setAccessible(true);
			if (field.get(userProfile) != null) {
				if ("newEmail".equals(field.getName())) {
					continue;
				} else if (field.getType().equals(String.class)) {
					statement.setString(counter, (String) field.get(userProfile));
				} else if (field.getType().equals(Integer.class)) {
					statement.setInt(counter, (Integer) field.get(userProfile));
				} else if (field.getType().equals(Boolean.class)) {
					statement.setBoolean(counter, (Boolean) field.get(userProfile));
				} else if (field.getType().equals(Float.class)) {
					statement.setFloat(counter, (Float) field.get(userProfile));
				} else {
					continue;
				}
				counter++;
			}
		}
		statement.setString(counter, login);
	}

	public static String generateInsertTagsRequest(InnerProfileDto profile) {
		StringBuilder sb = new StringBuilder();
		sb.append("delete from spark_db.t_tag where user_id=(select user_profile_id from spark_db.t_user_profile where login=?);");
		if (profile.getTags().size() > 0) {
			sb.append("with CTE as (\n" +
					"    select user_profile_id from spark_db.t_user_profile where login=?\n" +
					"    )\n" +
					"insert into spark_db.t_tag (name, user_id)\n" +
					"values ");
		}
		boolean first = true;
		for (String ignored : profile.getTags()) {
			if (first) {
				first = false;
			} else {
				sb.append(" ,");
			}
			sb.append("(?, (select user_profile_id from CTE))");
		}
		return sb.toString();
	}

	public static void addValuesToPreparedStatementTag(PreparedStatement statement, InnerProfileDto profile, String login)
			throws SQLException {
		statement.setString(1, login);
		if (profile.getTags().size() > 0) {
			statement.setString(2, login);
		}
		int counter = 3;
		for (String tag : profile.getTags()) {
			statement.setString(counter, tag);
			counter++;
		}
	}
}
