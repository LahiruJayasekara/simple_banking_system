package com.mlpj.simple.banking.system.service;

import com.mlpj.simple.banking.system.model.TransactionItem;
import com.mlpj.simple.banking.system.repo.TransactionsRepo;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.mlpj.simple.banking.system.util.Constants.STATEMENT_HEADERS_WITHOUT_BALANCE;

public class StatementService {

    private TransactionsRepo transactionsRepo = new TransactionsRepo();

    public String retrieveStatementForAccountWithoutBalance(String accountName) {

        List<TransactionItem> transactionsForAccount = transactionsRepo.getTransactionsForAccount(accountName);
        List<String> formattedTransactionList = transactionsForAccount.stream()
                .map(this::transactionToFormattedStingWithoutBalance).toList();

        StringBuilder statement = new StringBuilder();
        statement.append("Account: ").append(accountName).append(System.lineSeparator());
        statement.append(STATEMENT_HEADERS_WITHOUT_BALANCE).append(System.lineSeparator());

        formattedTransactionList.forEach(s -> statement.append(s).append(System.lineSeparator()));
        return statement.toString();
    }

    private String transactionToFormattedStingWithoutBalance(TransactionItem transactionItem) {
        return String.format("| %s | %s | %-4s | %11.2f |", transactionItem.getDate().format(DateTimeFormatter.BASIC_ISO_DATE), transactionItem.getTransactionID(),
                transactionItem.getTransactionType(), transactionItem.getAmount());
    }
}
