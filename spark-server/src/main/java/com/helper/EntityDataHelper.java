package com.helper;

import com.dictionary.Sex;
import com.dto.BaseDto;
import com.dto.BaseUserProfileDto;
import com.dto.FriendDto;
import com.dto.UserProfileDto;
import com.entity.TUserProfileEntity;

import java.util.ArrayList;
import java.util.List;

public class EntityDataHelper {
	public static UserProfileDto toDto(TUserProfileEntity entity) {
		if (entity == null) {
			return null;
		}
		UserProfileDto result = new UserProfileDto();
		result.setFirstName(entity.getFirstName());
		result.setLastName(entity.getLastName());
		result.setLogin(entity.getLogin());
		result.setPassword(entity.getPassword());
		result.setSex(toStringBoolean(entity.getSex()));
		result.setSexPreferences(toStringBoolean(entity.getSexPreferences()));
		result.setBiography(entity.getBiography());
		result.setEmail(entity.getEmail());
		result.setConfirmed(entity.isConfirmed());
		result.setConfirmedToken(entity.getConfirmedToken());
		result.setLocation(new float[] {entity.getLocation1().floatValue(), entity.getLocation2().floatValue()});
		result.setPhoto(-1);
		result.setRating(entity.getRating());
		result.setAge(entity.getAge());
		return result;
	}

	public static TUserProfileEntity toEntity(BaseUserProfileDto dto) {
		if (dto == null)
			return null;
		TUserProfileEntity entity = new TUserProfileEntity();
		entity.setLogin(dto.getLogin());
		entity.setPassword(dto.getPassword());
		entity.setEmail(dto.getEmail());
		if (dto.getSex() != null)
			entity.setSex(Sex.convertStringToCode(dto.getSex()));
		return entity;
	}

	public static FriendDto toFriendDto(TUserProfileEntity entity) {
		FriendDto friendDto = new FriendDto();
		friendDto.setLogin(entity.getLogin());
//		friendDto.set
		return friendDto;
	}

	private static String toStringBoolean(Integer i) {
		if (i == null)
			return null;
		if (i == 0) return "male";
		if (i == 1) return "female";
		return null;
	}
}
