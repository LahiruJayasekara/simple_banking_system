package com.mlpj.simple.banking.system.service;

import com.mlpj.simple.banking.system.model.TransactionItem;
import com.mlpj.simple.banking.system.util.Constants;

import static com.mlpj.simple.banking.system.cli.CLIUtils.displayOutput;
import static com.mlpj.simple.banking.system.cli.CLIUtils.makeUserPrompt;
import static com.mlpj.simple.banking.system.util.Constants.*;
import static com.mlpj.simple.banking.system.validations.InputValidator.validateInputForRegex;

public class InputHandler {

    private TransactionsService transactionsService = new TransactionsService();
    private StatementService statementService = new StatementService();
    private InterestService interestService = new InterestService();

    public boolean handleCommandInput(String initialPromptInput) {
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
