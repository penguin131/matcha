package com.service;

import com.dto.BaseUserProfileDto;

public interface Intra42Service {
	String getToken(String code) throws Exception;
	BaseUserProfileDto getCurrentUser(String token);
}
