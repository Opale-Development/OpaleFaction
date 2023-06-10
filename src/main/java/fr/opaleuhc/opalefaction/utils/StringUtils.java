package fr.opaleuhc.opalefaction.utils;

public class StringUtils {

    public static final String canContain = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_";

    public static boolean isStringValid(String str) {
        if (str.isEmpty()) return false;
        for (char c : str.toCharArray()) {
            if (!canContain.contains(String.valueOf(c))) return false;
        }
        return true;
    }
}
