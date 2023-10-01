package com.mlpj.simple.banking.system.service;

import com.mlpj.simple.banking.system.model.TransactionItem;
import com.mlpj.simple.banking.system.repo.TransactionsRepo;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.mlpj.simple.banking.system.util.Constants.STATEMENT_HEADERS_WITHOUT_BALANCE;
import static com.mlpj.simple.banking.system.util.Constants.STATEMENT_HEADERS_WITH_BALANCE;

public class StatementService {

    private TransactionsRepo transactionsRepo = new TransactionsRepo();
    private InterestService interestService = new InterestService();

    public String retrieveStatementForAccountWithoutBalance(String accountName) {

        List<TransactionItem> transactionsForAccount = transactionsRepo.getTransactionsForAccount(accountName);
        Stream<String> formattedList = transactionsForAccount.stream().map(this::transactionToFormattedStingWithoutBalance);
        return getFormattedStatement(accountName, formattedList, STATEMENT_HEADERS_WITHOUT_BALANCE);
    }

    public String retrieveStatementForAccount(String statementRequestInputString) {
        String[] statementRequestInfo = statementRequestInputString.split(" ");
        String accountName = statementRequestInfo[0];

        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyyMM");
        YearMonth yearMonth = YearMonth.parse(statementRequestInfo[1], inputFormat);

        TransactionItem interestTransaction = interestService.calculateInterestForMonth(accountName, statementRequestInfo[1]);
        List<TransactionItem> transactionsForAccount = new ArrayList<>(transactionsRepo.getTransactionsForMonth(accountName, yearMonth));

        transactionsForAccount.add(interestTransaction);
        Stream<String> formattedList = transactionsForAccount.stream().map(this::transactionToFormattedStingWithBalance);
        return getFormattedStatement(accountName, formattedList, STATEMENT_HEADERS_WITH_BALANCE);

    }

    private String getFormattedStatement(String accountName, Stream<String> stringStream, String statementHeadersWithBalance) {
        List<String> formattedTransactionList = stringStream.toList();

        StringBuilder statement = new StringBuilder();
        statement.append("Account: ").append(accountName).append(System.lineSeparator());
        statement.append(statementHeadersWithBalance).append(System.lineSeparator());

        formattedTransactionList.forEach(s -> statement.append(s).append(System.lineSeparator()));
        return statement.toString();
    }

    private String transactionToFormattedStingWithoutBalance(TransactionItem transactionItem) {
        return String.format("| %s | %s | %-4s | %11.2f |", transactionItem.getDate().format(DateTimeFormatter.BASIC_ISO_DATE), transactionItem.getTransactionID(),
                transactionItem.getTransactionType(), transactionItem.getAmount());
    }

    private String transactionToFormattedStingWithBalance(TransactionItem transactionItem) {
        return String.format("| %s | %11s | %-4s | %11.2f | %11.2f |", transactionItem.getDate().format(DateTimeFormatter.BASIC_ISO_DATE),
                transactionItem.getTransactionID() == null ? "" : transactionItem.getTransactionID(),
                transactionItem.getTransactionType(), transactionItem.getAmount(), transactionItem.getBalance());
    }
}
