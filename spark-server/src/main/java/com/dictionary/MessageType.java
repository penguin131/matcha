package com.dictionary;

public enum MessageType {
    CHAT_MESSAGE("chat_message"),
    NOTIFICATION("notification");

    private String name;

    MessageType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
