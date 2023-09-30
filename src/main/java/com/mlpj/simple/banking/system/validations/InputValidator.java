package com.mlpj.simple.banking.system.validations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidator {
    private InputValidator() {}

    public static boolean validateInputForRegex(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
}
