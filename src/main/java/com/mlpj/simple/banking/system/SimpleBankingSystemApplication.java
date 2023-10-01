package com.mlpj.simple.banking.system;

import com.mlpj.simple.banking.system.exception.InsufficientBalanceException;
import com.mlpj.simple.banking.system.exception.OlderTransactionDateException;
import com.mlpj.simple.banking.system.model.TransactionItem;
import com.mlpj.simple.banking.system.service.InterestService;
import com.mlpj.simple.banking.system.service.StatementService;
import com.mlpj.simple.banking.system.service.TransactionsService;
import com.mlpj.simple.banking.system.util.Constants;

import java.time.format.DateTimeParseException;

import static com.mlpj.simple.banking.system.cli.CLIUtils.displayOutput;
import static com.mlpj.simple.banking.system.cli.CLIUtils.makeUserPrompt;
import static com.mlpj.simple.banking.system.util.Constants.*;
import static com.mlpj.simple.banking.system.validations.InputValidator.validateInputForRegex;

public class SimpleBankingSystemApplication {

    private static TransactionsService transactionsService = new TransactionsService();
    private static StatementService statementService = new StatementService();
    private static InterestService interestService = new InterestService();

    public static void main(String[] args) {
        while (true) {
            try {
                String initialPromptInput = makeUserPrompt(INITIAL_PROMPT);
                if (validateInputForRegex(initialPromptInput, Constants.INITIAL_PROMPT_VALIDATION_REGEX)) {
                    if (!handleCommandInput(initialPromptInput)) break;
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

    private static boolean handleCommandInput(String initialPromptInput) {
        if (initialPromptInput.equalsIgnoreCase("T")) {
            String transactionInput = makeUserPrompt(INPUT_TRANSACTIONS_PROMPT);
            if (validateInputForRegex(transactionInput, INPUT_TRANSACTIONS_PROMPT_VALIDATION_REGEX)) {
                TransactionItem newTransaction = transactionsService.processTransaction(transactionInput);
                String statement = statementService.retrieveStatementForAccountWithoutBalance(newTransaction.getAccountName());
                displayOutput(statement);
            } else {
                displayOutput(INVALID_INPUT);
            }
        } else if (initialPromptInput.equalsIgnoreCase("I")) {
            String interestInput = makeUserPrompt(INPUT_INTEREST_RULE_PROMPT);
            if (validateInputForRegex(interestInput, INPUT_INTEREST_RULE_VALIDATION_REGEX)) {
                interestService.processInterestDefinition(interestInput);
                String interestListStatement = interestService.getInterestListStatement();
                displayOutput(interestListStatement);
            } else {
                displayOutput(INVALID_INPUT);
            }

        } else if (initialPromptInput.equalsIgnoreCase("P")) {
            String statementInput = makeUserPrompt(RETRIEVE_PRINT_STATEMENT_PROMPT);
            if (validateInputForRegex(statementInput, RETRIEVE_PRINT_STATEMENT_PROMPT_VALIDATION_REGEX)) {
                String statement = statementService.retrieveStatementForAccount(statementInput);
                displayOutput(statement);
            } else {
                displayOutput(INVALID_INPUT);
            }
        } else if (initialPromptInput.equalsIgnoreCase("Q")) {
            displayOutput(Constants.THANK_YOU_MESSAGE);
            return false;
        }
        return true;
    }
}
