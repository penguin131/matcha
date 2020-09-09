package com.helper;

import com.dto.UserProfileDto;
import com.entity.TUserProfileEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(JUnit4.class)
public class EntityDataHelperTest {

	//Не должно быть эксепшенов
	@Test
	public void initTest() {
		EntityDataHelper.toDto(new TUserProfileEntity());
	}

	private static final String LOGIN = "kkk";
	private static final String PASSWORD = "123";
	private static final String EMAIL = "lalala@lll.com";
	private static final String FIRST_NAME = "fff";
	private static final String LAST_NAME = "sss";
	private static final String TOKEN = "ssasdasdasdasdasddddddds";
	private static final BigDecimal COORDINATE = new BigDecimal("666.666");
	private static final int RATING = 666;
	private static final int AGE = 123;

	//проверяю заполнение полей и конвертацию
	@Test
	public void userProfileTest() {
		TUserProfileEntity user = new TUserProfileEntity();
		UserProfileDto dto1 = EntityDataHelper.toDto(user);
		UserProfileDto dto2 = new UserProfileDto();
		//пустой
		assertEquals(dto1, dto2);
		//логин
		user.setLogin(LOGIN);
		dto1 = EntityDataHelper.toDto(user);
		assertNotEquals(dto1, dto2);
		dto2.setLogin(LOGIN);
		assertEquals(dto1, dto2);
		//пароль
		user.setPassword(PASSWORD);
		dto1 = EntityDataHelper.toDto(user);
		assertNotEquals(dto1, dto2);
		dto2.setPassword(PASSWORD);
		assertEquals(dto1, dto2);
		//почта
		user.setEmail(EMAIL);
		dto1 = EntityDataHelper.toDto(user);
		assertNotEquals(dto1, dto2);
		dto2.setEmail(EMAIL);
		assertEquals(dto1, dto2);
		//firstName
		user.setFirstName(FIRST_NAME);
		dto1 = EntityDataHelper.toDto(user);
		assertNotEquals(dto1, dto2);
		dto2.setFirstName(FIRST_NAME);
		assertEquals(dto1, dto2);
		//lastName
		user.setLastName(LAST_NAME);
		dto1 = EntityDataHelper.toDto(user);
		assertNotEquals(dto1, dto2);
		dto2.setLastName(LAST_NAME);
		assertEquals(dto1, dto2);
		//confirmedToken
		user.setConfirmedToken(TOKEN);
		dto1 = EntityDataHelper.toDto(user);
		assertNotEquals(dto1, dto2);
		dto2.setConfirmedToken(TOKEN);
		assertEquals(dto1, dto2);
		//coordinates
		user.setLocation1(COORDINATE);
		user.setLocation2(COORDINATE);
		dto1 = EntityDataHelper.toDto(user);
		assertNotEquals(dto1, dto2);
		dto2.setLocation(new float[] {COORDINATE.floatValue(), COORDINATE.floatValue()});
		assertEquals(dto1, dto2);
		//photo
		user.setPhotos(new ArrayList<>());

		//rating
		user.setRating(RATING);
		dto1 = EntityDataHelper.toDto(user);
		assertNotEquals(dto1, dto2);
		dto2.setRating(RATING);
		assertEquals(dto1, dto2);
		//age
		user.setAge(AGE);
		dto1 = EntityDataHelper.toDto(user);
		assertNotEquals(dto1, dto2);
		dto2.setAge(AGE);
		assertEquals(dto1, dto2);
	}
}
