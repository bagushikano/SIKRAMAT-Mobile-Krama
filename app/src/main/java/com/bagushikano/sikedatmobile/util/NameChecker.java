package com.bagushikano.sikedatmobile.util;

public final class NameChecker {
    public static boolean isNameValid(String text){
        return text.matches("^([A-Za-z]+)(\\s[A-Za-z]+)*\\s?$");
    }
}
