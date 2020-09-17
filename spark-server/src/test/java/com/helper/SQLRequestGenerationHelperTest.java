package com.helper;

import com.dto.UserFilterDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class SQLRequestGenerationHelperTest {

	private final String startText1 = "with CTE as (\n" +
			"    select location_1, location_2 from \"spark_db\".t_user_profile where login='";
	private final String startText2 = "'" +
			" )" +
			" select *," +
			" (select id_image from \"spark_db\".t_image " +
			" where user_profile_id=user_id and is_main=true limit 1) as photo " +
			" ,(select  count(*) from spark_db.t_users_unity where user_profile_id=user2_id or (user_profile_id=user1_id and t_user_profile.confirmed=true)) as has_like" +
			" ,(select  count(*) from spark_db.t_complaint where user_profile_id=to_user) as has_dislike" +
			" from \"spark_db\".t_user_profile";
	private final String login = "123";
	private final String withoutLogin = startText1 + null + startText2;
	private final String withLogin = startText1 + login + startText2 + " where login<>?";
	private final String checkFunction = " wsg84_check_distance(" +
			" (select CTE.location_1 from CTE limit 1)," +
			" (select CTE.location_2 from CTE limit 1)," +
			" location_1,location_2,?)=true";
	private final String standardOrderBy = " order by rating desc\n" +
			",(select count(*) from spark_db.t_tag t1, spark_db.t_tag t2\n" +
			" where t1.user_id=(select user_profile_id from CTE) and t2.user_id=u.user_profile_id and t1.name=t2.name)";

	@Test
	public void generateUserSearchRequestTest() {
		//zero login
		assertEquals(withoutLogin + standardOrderBy,
				SQLRequestGenerationHelper.generateUserSearchRequest(null, null));
		//non-zero login
		assertEquals(withLogin + standardOrderBy,
				SQLRequestGenerationHelper.generateUserSearchRequest(null, login));
	}

	@Test
	public void generateUserSearchRequestWithFilterTest() {
		//zero login
		UserFilterDto filter1 = new UserFilterDto();
		assertEquals(withoutLogin + standardOrderBy,
				SQLRequestGenerationHelper.generateUserSearchRequest(filter1, null));
		filter1.setRating(1);
		assertEquals(withoutLogin + " where rating>=?" + standardOrderBy,
				SQLRequestGenerationHelper.generateUserSearchRequest(filter1, null));
		filter1.setSex("male");
		assertEquals(withoutLogin + " where sex=? and rating>=?" + standardOrderBy,
				SQLRequestGenerationHelper.generateUserSearchRequest(filter1, null));
		filter1.setAgeMin(1);
		assertEquals(withoutLogin + " where sex=? and rating>=? and age>=?" + standardOrderBy,
				SQLRequestGenerationHelper.generateUserSearchRequest(filter1, null));
		filter1.setAgeMax(2);
		assertEquals(withoutLogin + " where sex=? and rating>=? and age<=? and age>=?" + standardOrderBy,
				SQLRequestGenerationHelper.generateUserSearchRequest(filter1, null));
		filter1.setSexPreferences("male");
		assertEquals(withoutLogin + " where (sex_preferences=? or sex_preferences is null) and sex=? and rating>=? and age<=? and age>=?" + standardOrderBy,
				SQLRequestGenerationHelper.generateUserSearchRequest(filter1, null));
		filter1.setDistance(1);
		assertEquals(withoutLogin + " where" + checkFunction + " and (sex_preferences=? or sex_preferences is null) and sex=? and rating>=? and age<=? and age>=?" + standardOrderBy,
				SQLRequestGenerationHelper.generateUserSearchRequest(filter1, null));

		//non-zero login
		String login = "123";
		UserFilterDto filter2 = new UserFilterDto();
		assertEquals(withLogin + standardOrderBy,
				SQLRequestGenerationHelper.generateUserSearchRequest(filter2, login));
		filter2.setRating(1);
		assertEquals(withLogin + " and rating>=?" + standardOrderBy,
				SQLRequestGenerationHelper.generateUserSearchRequest(filter2, login));
		filter2.setSex("male");
		assertEquals(withLogin + " and sex=? and rating>=?" + standardOrderBy,
				SQLRequestGenerationHelper.generateUserSearchRequest(filter2, login));
		filter2.setAgeMin(1);
		assertEquals(withLogin + " and sex=? and rating>=? and age>=?" + standardOrderBy,
				SQLRequestGenerationHelper.generateUserSearchRequest(filter2, login));
		filter2.setAgeMax(2);
		assertEquals(withLogin + " and sex=? and rating>=? and age<=? and age>=?" + standardOrderBy,
				SQLRequestGenerationHelper.generateUserSearchRequest(filter2, login));
		filter2.setSexPreferences("male");
		assertEquals(withLogin + " and (sex_preferences=? or sex_preferences is null) and sex=? and rating>=? and age<=? and age>=?" + standardOrderBy,
				SQLRequestGenerationHelper.generateUserSearchRequest(filter2, login));
		filter2.setDistance(1);
		assertEquals(withLogin + " and" + checkFunction + " and (sex_preferences=? or sex_preferences is null) and sex=? and rating>=? and age<=? and age>=?" + standardOrderBy,
				SQLRequestGenerationHelper.generateUserSearchRequest(filter2, login));
	}
}
