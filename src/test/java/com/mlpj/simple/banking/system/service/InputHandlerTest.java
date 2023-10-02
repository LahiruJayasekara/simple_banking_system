package com.mlpj.simple.banking.system.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.Charset;

import static com.mlpj.simple.banking.system.TestUtils.getInputHandler;
import static com.mlpj.simple.banking.system.util.Constants.*;

class InputHandlerTest {

    @Test
    void testHandleValidTransactionInput() throws IOException {
        try (InputStream inputStream = new ByteArrayInputStream("20230505 AC001 D 100.00".getBytes());
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            System.setIn(inputStream);

            PrintStream out = new PrintStream(byteArrayOutputStream);
            System.setOut(out);

            InputHandler inputHandler = getInputHandler();
            boolean valid = inputHandler.handleCommandInput("T");
            Assertions.assertTrue(valid);

            String output = byteArrayOutputStream.toString(Charset.defaultCharset());
            String expectedStatement = INPUT_TRANSACTIONS_PROMPT + System.lineSeparator() +
                    "Account: AC001" + System.lineSeparator() +
                    "| Date     | Txn Id      | Type |    Amount   |" + System.lineSeparator() +
                    "| 20230505 | 20230505-01 | D    |      100.00 |" + System.lineSeparator();
            Assertions.assertEquals(expectedStatement.trim(), output.trim());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testHandleInValidTransactionInput() throws IOException, IllegalAccessException {
        try (InputStream inputStream = new ByteArrayInputStream("20230505 AC001 D rs100.00".getBytes());
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            System.setIn(inputStream);

            PrintStream out = new PrintStream(byteArrayOutputStream);
            System.setOut(out);

            InputHandler inputHandler = getInputHandler();
            boolean valid = inputHandler.handleCommandInput("T");
            Assertions.assertTrue(valid);

            String output = byteArrayOutputStream.toString(Charset.defaultCharset());
            String expectedStatement = INPUT_TRANSACTIONS_PROMPT + System.lineSeparator() + INVALID_INPUT;
            Assertions.assertEquals(expectedStatement.trim(), output.trim());
        }
    }

    @Test
    void testHandleValidInterestInput() throws IOException, IllegalAccessException {
        try (InputStream inputStream = new ByteArrayInputStream("20230520 RULE02 1.90".getBytes());
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            System.setIn(inputStream);

            PrintStream out = new PrintStream(byteArrayOutputStream);
            System.setOut(out);

            InputHandler inputHandler = getInputHandler();
            boolean valid = inputHandler.handleCommandInput("I");
            Assertions.assertTrue(valid);

            String output = byteArrayOutputStream.toString(Charset.defaultCharset());
            String expectedStatement = INPUT_INTEREST_RULE_PROMPT + System.lineSeparator() +
                    "Interest rules:" + System.lineSeparator() +
                    "| Date     | RuleId               | Rate(%) |" + System.lineSeparator() +
                    "| 20230520 | RULE02               |    1.90 |" + System.lineSeparator();
            Assertions.assertEquals(expectedStatement.trim(), output.trim());
        }
    }

    @Test
    void testHandleInValidInterestInput() throws IOException, IllegalAccessException {
        try (InputStream inputStream = new ByteArrayInputStream("20230520 RULE02 1.90%".getBytes());
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            System.setIn(inputStream);

            PrintStream out = new PrintStream(byteArrayOutputStream);
            System.setOut(out);

            InputHandler inputHandler = getInputHandler();
            boolean valid = inputHandler.handleCommandInput("I");
            Assertions.assertTrue(valid);

            String output = byteArrayOutputStream.toString(Charset.defaultCharset());
            String expectedStatement = INPUT_INTEREST_RULE_PROMPT + System.lineSeparator() + INVALID_INPUT;
            Assertions.assertEquals(expectedStatement.trim(), output.trim());
        }
    }

    @Test
    void testHandleValidStatementInput() throws IOException, IllegalAccessException {
        try (InputStream inputStream = new ByteArrayInputStream("AC001 202306".getBytes());
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            System.setIn(inputStream);

            PrintStream out = new PrintStream(byteArrayOutputStream);
            System.setOut(out);

            InputHandler inputHandler = getInputHandler();
            boolean valid = inputHandler.handleCommandInput("P");
            Assertions.assertTrue(valid);

            String output = byteArrayOutputStream.toString(Charset.defaultCharset());
            String expectedStatement = RETRIEVE_PRINT_STATEMENT_PROMPT + System.lineSeparator() +
                    "Account: AC001" + System.lineSeparator() +
                    "| Date     | Txn Id      | Type |    Amount   |   Balance   |" + System.lineSeparator();
            Assertions.assertEquals(expectedStatement.trim(), output.trim());
        }
    }

    @Test
    void testHandleInValidStatementInput() throws IOException, IllegalAccessException {
        try (InputStream inputStream = new ByteArrayInputStream("AC001 a202306".getBytes());
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            System.setIn(inputStream);

            PrintStream out = new PrintStream(byteArrayOutputStream);
            System.setOut(out);

            InputHandler inputHandler = getInputHandler();
            boolean valid = inputHandler.handleCommandInput("p");
            Assertions.assertTrue(valid);

            String output = byteArrayOutputStream.toString(Charset.defaultCharset());
            String expectedStatement = RETRIEVE_PRINT_STATEMENT_PROMPT + System.lineSeparator() + INVALID_INPUT;
            Assertions.assertEquals(expectedStatement.trim(), output.trim());
        }
    }

    @Test
    void testHandleQuitInput() throws IOException, IllegalAccessException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            PrintStream out = new PrintStream(byteArrayOutputStream);
            System.setOut(out);

            InputHandler inputHandler = getInputHandler();
            boolean valid = inputHandler.handleCommandInput("Q");
            Assertions.assertFalse(valid);

            String output = byteArrayOutputStream.toString(Charset.defaultCharset());
            Assertions.assertEquals(THANK_YOU_MESSAGE.trim(), output.trim());
        }
    }
}
