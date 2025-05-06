package com.example.cabinetmedical.utils;


import com.google.i18n.phonenumbers.PhoneNumberUtil;
import java.util.regex.Pattern;

public class check {

    private static final Pattern STRICT_EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                    "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    public static boolean phoneNumber(String phoneNumber, String countryCode) {
        PhoneNumberUtil util = PhoneNumberUtil.getInstance();
        try {
            if (!phoneNumber.startsWith("+")) {
                phoneNumber = "+" + countryCode + phoneNumber;
            }
            return util.isValidNumber(util.parse(phoneNumber, countryCode));
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean Email(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return STRICT_EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean Password(String password){
        if (password == null || password.isEmpty()) {
            return false;
        }

        // Check length
        if (password.length() < 8) {
            return false;
        }

        // Check for at least 1 digit
        if (!password.matches(".*\\d.*")) {
            return false;
        }

        // Check for at least 1 letter
        if (!password.matches(".*[a-zA-Z].*")) {
            return false;
        }

        // Check for at least 1 special character
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            return false;
        }

        // Check no whitespace
        if (password.matches(".*\\s.*")) {
            return false;
        }

        return true;
    }

}
