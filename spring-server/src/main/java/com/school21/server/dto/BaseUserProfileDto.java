package com.school21.server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseUserProfileDto extends CredentialsDto {

    private String email;
    private String sex;
    @JsonProperty("intra_login")
    private String intraLogin;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIntraLogin() {
        return intraLogin;
    }

    public void setIntraLogin(String intraLogin) {
        this.intraLogin = intraLogin;
    }
}
