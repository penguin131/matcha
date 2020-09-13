package com.dto;

import org.apache.commons.lang.StringUtils;

public class CredentialsDto implements BaseDto {
    private String login;
    private String password;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CredentialsDto))
            return false;
        CredentialsDto other = (CredentialsDto) obj;
        return StringUtils.equals(login, other.login) &&
                StringUtils.equals(password, other.password);
    }
}
