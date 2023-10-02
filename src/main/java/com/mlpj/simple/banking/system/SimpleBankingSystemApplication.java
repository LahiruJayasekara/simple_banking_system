package com.mlpj.simple.banking.system;

import com.mlpj.simple.banking.system.exception.InsufficientBalanceException;
import com.mlpj.simple.banking.system.exception.OlderTransactionDateException;
import com.mlpj.simple.banking.system.service.InputHandler;
import com.mlpj.simple.banking.system.util.Constants;

import java.time.format.DateTimeParseException;

import static com.mlpj.simple.banking.system.cli.CLIUtils.displayOutput;
import static com.mlpj.simple.banking.system.cli.CLIUtils.makeUserPrompt;
import static com.mlpj.simple.banking.system.util.Constants.*;
import static com.mlpj.simple.banking.system.validations.InputValidator.validateInputForRegex;

public class SimpleBankingSystemApplication {

    private static InputHandler inputHandler = new InputHandler();

    public static void main(String[] args) {
        while (true) {
            try {
                String initialPromptInput = makeUserPrompt(INITIAL_PROMPT);
                if (validateInputForRegex(initialPromptInput, Constants.INITIAL_PROMPT_VALIDATION_REGEX)) {
                    if (!inputHandler.handleCommandInput(initialPromptInput)) break;
                } else {
                    displayOutput(Constants.INVALID_INPUT);
                }
            } catch (InsufficientBalanceException e) {
                displayOutput(INSUFFICIENT_BALANCE);
            } catch (OlderTransactionDateException e) {
                displayOutput(OLDER_TRANSACTION_DATE);
            } catch (DateTimeParseException | NumberFormatException e) {
                displayOutput(Constants.INVALID_INPUT);
            }
        }
    }
}
