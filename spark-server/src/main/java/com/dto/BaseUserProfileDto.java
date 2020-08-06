package com.dto;

public class BaseUserProfileDto extends CredentialsDto {

    private String email;
    private String sex;

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
}
