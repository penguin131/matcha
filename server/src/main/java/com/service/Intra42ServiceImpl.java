package com.service;

import com.dto.BaseUserProfileDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
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
	private static final String API42_CURRENT_USER = "https://api.intra.42.fr/v2/me/";
	private static final String CLIENT_ID = "7ff75fdfa415c5709f7d9257bc163dbd22654eae9a10799daffeb52026b924ac";
	private static final String CLIENT_SECRET = "d1ff52f973b3b41004e043c783cc6c23a5c6b30d4424b98baabd2fbc83dcf164";
	private static final String GRANT_TYPE = "authorization_code";
	private static final String REDIRECT_URI = "http%3A%2F%2Flocalhost%3A3000%2Fredirect";

	@Override
	public String getToken(String code) throws Exception {
		logger.info(String.format("getToken(%s)", code));
		HttpPost post = new HttpPost(API42_TOKEN +
				"?grant_type=" + GRANT_TYPE +
				"&client_id=" + CLIENT_ID +
				"&client_secret=" + CLIENT_SECRET +
				"&code=" + code +
				"&redirect_uri=" + REDIRECT_URI);
		logger.info("post: " + post.toString());
		try (CloseableHttpClient httpClient = HttpClients.custom()
							.setDefaultRequestConfig(RequestConfig.custom()
							.setCookieSpec(CookieSpecs.STANDARD).build())
							.build();
			 CloseableHttpResponse response = httpClient.execute(post)) {
			String result = EntityUtils.toString(response.getEntity());
			logger.info("Result: " + result);
			ObjectNode node = mapper.readValue(result, ObjectNode.class);
			JsonNode jsonNode = node.get("access_token");
			if (jsonNode != null) {
				return jsonNode.asText();
			}
		}
		throw new Exception("Error getting token from intra42");
	}

	@Override
	public BaseUserProfileDto getCurrentUser(String token) throws Exception {
		logger.info(String.format("getCurrentUser(%s)", token));
		HttpGet get = new HttpGet(API42_CURRENT_USER);
		get.setHeader("Authorization", "Bearer " + token);
		try (CloseableHttpClient httpClient = HttpClients.custom()
						.setDefaultRequestConfig(RequestConfig.custom()
						.setCookieSpec(CookieSpecs.STANDARD).build())
						.build();
			 CloseableHttpResponse response = httpClient.execute(get)) {
			String result = EntityUtils.toString(response.getEntity());
			ObjectNode node = mapper.readValue(result, ObjectNode.class);
			BaseUserProfileDto profile = new BaseUserProfileDto();
			profile.setLogin(node.get("login").asText());
			profile.setEmail(node.get("email").asText());
			profile.setSex("male");
			profile.setPassword("oauth2");
			profile.setIntraLogin(profile.getLogin());
			return profile;
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		throw new Exception("Intra API error");
	}
}
