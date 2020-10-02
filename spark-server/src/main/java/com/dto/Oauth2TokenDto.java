package com.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Oauth2TokenDto {
	@JsonProperty("access_token")
	private String accessToken;
	@JsonProperty("token_type")
	private String tokenType;
	@JsonProperty("expires_in")
	private Integer expiresIn;
	@JsonProperty("refresh_token")
	private String refreshToken;
	private String scope;
	@JsonProperty("created_at")
	private Integer createdAt;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public Integer getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(Integer expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public Integer getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Integer createdAt) {
		this.createdAt = createdAt;
	}
}
