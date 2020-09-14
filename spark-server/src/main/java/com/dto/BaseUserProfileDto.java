package com.dto;

import org.apache.commons.lang.StringUtils;

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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BaseUserProfileDto))
            return false;
        BaseUserProfileDto other = (BaseUserProfileDto) obj;
        return super.equals(other) &&
                StringUtils.equals(email, other.getEmail()) &&
                StringUtils.equals(sex, other.getSex());
    }
}
