package com.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthDataDto extends CredentialsDto {
	@JsonProperty("oauth2_code")
	private String oauth2Code;

	public String getOauth2Code() {
		return oauth2Code;
	}

	public void setOauth2Code(String oauth2Code) {
		this.oauth2Code = oauth2Code;
	}
}
