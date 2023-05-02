package com.example.webapplication.utils;

import java.util.regex.Pattern;

public class EmailValidation {
    public boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }
}
