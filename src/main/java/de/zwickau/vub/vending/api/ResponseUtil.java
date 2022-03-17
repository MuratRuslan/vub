package de.zwickau.vub.vending.api;

public class ResponseUtil {

    public static String createJsonWithMessage(String message) {
        return "{ \"message\": \"" + message+"\" }";
    }
}
