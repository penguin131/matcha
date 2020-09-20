package com.helper;

import com.dto.UserFilterDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class SQLRequestGenerationHelperTest {

	private final String startText1 = "with CTE as (\n" +
			"    select user_profile_id, latitude, longitude from \"spark_db\".t_user_profile where login='";
	private final String startText2 = "'\n" +
			"    )\n" +
			"   , CTE2 as (\n" +
			"    select\n" +
			"         user_profile_id\n" +
			"         ,wsg84_get_distance( (select CTE.latitude from CTE limit 1), (select CTE.longitude from CTE limit 1), latitude, longitude) as distance\n" +
			"    from spark_db.t_user_profile\n" +
			")\n" +
			"select *\n" +
			"     ,(select id_image from \"spark_db\".t_image  where u.user_profile_id=user_id and is_main=true limit 1) as photo\n" +
			"     ,(select  count(*) from spark_db.t_users_unity where u.user_profile_id=user2_id or (u.user_profile_id=user1_id and confirmed=true)) as has_like\n" +
			"     ,(select  count(*) from spark_db.t_complaint where u.user_profile_id=to_user) as has_dislike\n" +
			"     ,CTE2.distance\n" +
			"from \"spark_db\".t_user_profile u\n" +
			"join CTE2 on CTE2.user_profile_id=u.user_profile_id";
	private final String login = "123";
	private final String withoutLogin = startText1 + null + startText2;
	private final String withLogin = startText1 + login + startText2 + " where login<>?";
	private final String standardOrderBy = " order by rating desc\n" +
			",(select count(*) from spark_db.t_tag t1, spark_db.t_tag t2\n" +
			"    where t1.user_id=(select user_profile_id from CTE) and t2.user_id=u.user_profile_id and t1.name=t2.name) desc\n" +
			",CTE2.distance";

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
		assertEquals(withoutLogin + " where CTE2.distance<=? and (sex_preferences=? or sex_preferences is null) and sex=? and rating>=? and age<=? and age>=?" + standardOrderBy,
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
		assertEquals(withLogin + " and CTE2.distance<=? and (sex_preferences=? or sex_preferences is null) and sex=? and rating>=? and age<=? and age>=?" + standardOrderBy,
				SQLRequestGenerationHelper.generateUserSearchRequest(filter2, login));
	}
}
