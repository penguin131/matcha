package com.dto;

import java.sql.Date;

public class FriendDto {
    private String login;
    private String lastMessage;
    private Date date;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public FriendDto(String login, String lastMessage, Date date) {
        this.login = login;
        this.lastMessage = lastMessage;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
