package com.mlpj.simple.banking.system.validations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.mlpj.simple.banking.system.util.Constants.*;
import static com.mlpj.simple.banking.system.validations.InputValidator.validateInputForRegex;

class InputValidatorTest {

    @Test
    void testValidateInputForRegex() {
        // initial prompt validations
        boolean isValid1 = validateInputForRegex("T", INITIAL_PROMPT_VALIDATION_REGEX);
        Assertions.assertTrue(isValid1);
        boolean isValid2 = validateInputForRegex("t", INITIAL_PROMPT_VALIDATION_REGEX);
        Assertions.assertTrue(isValid2);
        boolean isValid3 = validateInputForRegex("I", INITIAL_PROMPT_VALIDATION_REGEX);
        Assertions.assertTrue(isValid3);
        boolean isValid4 = validateInputForRegex("i", INITIAL_PROMPT_VALIDATION_REGEX);
        Assertions.assertTrue(isValid4);
        boolean isValid5 = validateInputForRegex("P", INITIAL_PROMPT_VALIDATION_REGEX);
        Assertions.assertTrue(isValid5);
        boolean isValid6 = validateInputForRegex("p", INITIAL_PROMPT_VALIDATION_REGEX);
        Assertions.assertTrue(isValid6);
        boolean isValid7 = validateInputForRegex("Q", INITIAL_PROMPT_VALIDATION_REGEX);
        Assertions.assertTrue(isValid7);
        boolean isValid8 = validateInputForRegex("q", INITIAL_PROMPT_VALIDATION_REGEX);
        Assertions.assertTrue(isValid8);

        // validate input transaction
        boolean isValid9 = validateInputForRegex("20230626 AC001 W 100.00", INPUT_TRANSACTIONS_PROMPT_VALIDATION_REGEX);
        Assertions.assertTrue(isValid9);
        boolean isValid10 = validateInputForRegex("20230626 AC001 D 100.00", INPUT_TRANSACTIONS_PROMPT_VALIDATION_REGEX);
        Assertions.assertTrue(isValid10);

        // validate define interest input
        boolean isValid11 = validateInputForRegex("20230615 RULE03 2.20", INPUT_INTEREST_RULE_VALIDATION_REGEX);
        Assertions.assertTrue(isValid11);

        // validate print statement input
        boolean isValid12 = validateInputForRegex("AC001 202306", RETRIEVE_PRINT_STATEMENT_PROMPT_VALIDATION_REGEX);
        Assertions.assertTrue(isValid12);
    }

    @Test
    void testInvalidInputForRegex() {
        // initial prompt validations
        boolean isValid1 = validateInputForRegex("a", INITIAL_PROMPT_VALIDATION_REGEX);
        Assertions.assertFalse(isValid1);
        boolean isValid2 = validateInputForRegex("A", INITIAL_PROMPT_VALIDATION_REGEX);
        Assertions.assertFalse(isValid2);
        boolean isValid3 = validateInputForRegex("Aa", INITIAL_PROMPT_VALIDATION_REGEX);
        Assertions.assertFalse(isValid3);

        // validate input transaction
        boolean isValid9 = validateInputForRegex("202306260 AC001 W 100.00", INPUT_TRANSACTIONS_PROMPT_VALIDATION_REGEX);
        Assertions.assertFalse(isValid9);
        boolean isValid10 = validateInputForRegex("20230626 AC001 R 100.00", INPUT_TRANSACTIONS_PROMPT_VALIDATION_REGEX);
        Assertions.assertFalse(isValid10);
        boolean isValid8 = validateInputForRegex("20230626 AC001 W USD100.00", INPUT_TRANSACTIONS_PROMPT_VALIDATION_REGEX);
        Assertions.assertFalse(isValid8);

        // validate define interest input
        boolean isValid11 = validateInputForRegex("2023060 RULE03 2.20", INPUT_INTEREST_RULE_VALIDATION_REGEX);
        Assertions.assertFalse(isValid11);
        boolean isValid17 = validateInputForRegex("202306 RULE03 2.20%", INPUT_INTEREST_RULE_VALIDATION_REGEX);
        Assertions.assertFalse(isValid17);

        // validate print statement input
        boolean isValid12 = validateInputForRegex("AC001 20230601", RETRIEVE_PRINT_STATEMENT_PROMPT_VALIDATION_REGEX);
        Assertions.assertFalse(isValid12);
    }
}
