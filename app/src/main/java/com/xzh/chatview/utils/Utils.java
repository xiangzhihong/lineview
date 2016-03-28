package com.xzh.chatview.utils;

public class Utils {

    public static Integer toInteger(Object value) {
        try {
            if (value instanceof Integer) {
                return (Integer) value;
            } else if (value instanceof Number) {
                return ((Number) value).intValue();
            } else if (value instanceof CharSequence) {
                return Integer.valueOf(value.toString());
            }
        } catch (Exception e) {
            return 0;
        }
        return 0;
    }
}
