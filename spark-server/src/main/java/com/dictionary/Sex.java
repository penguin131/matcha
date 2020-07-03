package com.dictionary;

public enum Sex {
    MAN,
    WOMAN;

    public static boolean containsCode(Integer code) {
        if (code == null)
            return false;
        for (Sex number : Sex.values()) {
            if (number.ordinal() == code)
                return true;
        }
        return false;
    }
}
