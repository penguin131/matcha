package com.school21.server.dictionary;

public enum Sex {
    MAN(0, "male"),
    WOMAN(1, "female"),
    UNDEFINED(2, "");

    private final int code;
    private final String name;

    Sex(int code, String name) {
        this.code = code;
        this.name = name;
    }


    public static boolean containsCode(String code) {
        return !"male".equals(code) && !"female".equals(code);
    }

    public static String convertCodeToString(int code) {
        if (code == 0) return "male";
        if (code == 1) return "female";
        return null;
    }

    public static int convertStringToCode(String code) {
        if ("male".equals(code)) return 0;
        if ("female".equals(code)) return 1;
        return -1;
    }
}
