package com.dto;

public class MessageDto {
    private String type;
    private String msgText;
    private String from;
    private String to;
    private long date;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMsgText() {
        return msgText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public MessageDto(String type,
                      String msgText,
                      String from,
                      String to,
                      long date) {

        this.type = type;
        this.msgText = msgText;
        this.from = from;
        this.to = to;
        this.date = date;
    }

    public MessageDto(String msgText,
                      String from,
                      String to,
                      long date) {

        this.msgText = msgText;
        this.from = from;
        this.to = to;
        this.date = date;
    }

    public MessageDto() {

    }
}
