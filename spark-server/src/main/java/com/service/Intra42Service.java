package com.service;

import java.io.IOException;

public interface Intra42Service {
	String getToken(String code) throws IOException;
	String getUserForId(String token, String login);
}
