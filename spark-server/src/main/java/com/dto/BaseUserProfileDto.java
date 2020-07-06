package com.dto;

public class BaseUserProfileDto extends CredentialsDto {

    private String email;
    private int sex;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
