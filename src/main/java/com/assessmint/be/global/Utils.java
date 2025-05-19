package com.assessmint.be.global;

public class Utils {
    public static Boolean parseBoolean(String value) {
        if (value == null)
            return null;

        final var tempval = value.toLowerCase();

        final var isTrue = tempval.equals("true");
        final var isFalse = tempval.equals("false");

        if (isTrue || isFalse)
            return isTrue;

        return null;
    }

    public static String capitalizeName(String name) {
        if (name == null || name.isEmpty())
            return name;

        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }
}