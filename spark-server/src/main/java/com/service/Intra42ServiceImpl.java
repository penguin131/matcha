package com.service;

import com.dto.Oauth2TokenDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;

public class Intra42ServiceImpl implements Intra42Service {
	private Logger logger = Logger.getLogger(Intra42ServiceImpl.class);
	private static ObjectMapper mapper = new ObjectMapper();
	private static final String API42_TOKEN = "https://api.intra.42.fr/oauth/token";
	private static final String API42_GET_USER = "https://api.intra.42.fr/v2/users/";
	private static final String CLIENT_ID = "7ff75fdfa415c5709f7d9257bc163dbd22654eae9a10799daffeb52026b924ac";
	private static final String CLIENT_SECRET = "d1ff52f973b3b41004e043c783cc6c23a5c6b30d4424b98baabd2fbc83dcf164";
	private static final String GRANT_TYPE = "authorization_code";
	private static final String REDIRECT_URI = "http%3A%2F%2Flocalhost%3A8080";

	@Override
	public String getToken(String code) {
		logger.info(String.format("getToken(%s)", code));
		HttpPost post = new HttpPost(API42_TOKEN +
				"?grant_type=" + GRANT_TYPE +
				"&client_id=" + CLIENT_ID +
				"&client_secret=" + CLIENT_SECRET +
				"&code=" + code +
				"&redirect_uri=" + REDIRECT_URI);
		try (CloseableHttpClient httpClient = HttpClients.createDefault();
			 CloseableHttpResponse response = httpClient.execute(post)) {
			String result = EntityUtils.toString(response.getEntity());
			logger.info("Result: " + result);
			Oauth2TokenDto tokenDto = mapper.readValue(result, Oauth2TokenDto.class);
			return tokenDto.getAccessToken();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public String getUserForId(String token, String id) {
		logger.info(String.format("getUserForLogin(token, %s)", id));
		HttpGet get = new HttpGet(API42_GET_USER + id + "/?access_token=" + token);
		try (CloseableHttpClient httpClient = HttpClients.createDefault();
			 CloseableHttpResponse response = httpClient.execute(get)){
			String result = EntityUtils.toString(response.getEntity());
			logger.info("Result: " + result);

		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return null;
	}
}
