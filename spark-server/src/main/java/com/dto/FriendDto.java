package com.dto;

public class FriendDto {
    private String login;
    private String lastMessage;
    private long date;

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

    public FriendDto(String login, String lastMessage, long date) {
        this.login = login;
        this.lastMessage = lastMessage;
        this.date = date;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
