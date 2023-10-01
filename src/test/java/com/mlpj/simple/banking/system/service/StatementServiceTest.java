package com.mlpj.simple.banking.system.service;

import com.mlpj.simple.banking.system.model.InterestItem;
import com.mlpj.simple.banking.system.model.TransactionItem;
import com.mlpj.simple.banking.system.repo.TransactionsRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.mlpj.simple.banking.system.TestUtils.*;

class StatementServiceTest {

    @Test
    void testRetrieveStatementForAccountWithoutBalance() throws IllegalAccessException {
        TransactionItem transactionItem0 = new TransactionItem("20230929-05", LocalDate.parse("20230929",
                DateTimeFormatter.BASIC_ISO_DATE), "AC001", TransactionItem.TransactionType.D, 150.00, 150.00);
        TransactionItem transactionItem1 = new TransactionItem("20230930-01", LocalDate.parse("20230930",
                DateTimeFormatter.BASIC_ISO_DATE), "AC001", TransactionItem.TransactionType.D, 150.00, 150.00);
        TransactionItem transactionItem2 = new TransactionItem("20230930-02", LocalDate.parse("20230930",
                DateTimeFormatter.BASIC_ISO_DATE), "AC001", TransactionItem.TransactionType.W, 50.00, 100.00);
        TransactionItem transactionItemDifferentAccount = new TransactionItem("20230930-01", LocalDate.parse("20230930",
                DateTimeFormatter.BASIC_ISO_DATE), "AC002", TransactionItem.TransactionType.D, 150.00, 150.00);
        List<TransactionItem> transactionList = new ArrayList<>();
        transactionList.add(transactionItem0);
        transactionList.add(transactionItem1);
        transactionList.add(transactionItem2);
        transactionList.add(transactionItemDifferentAccount);

        TransactionsRepo transactionsRepo = getTransactionRepo(transactionList);
        StatementService statementService = getStatementService(transactionsRepo,
                getInterestService(getInterestRepo(new ArrayList<>()), transactionsRepo));

        String statement = statementService.retrieveStatementForAccountWithoutBalance("AC001");

        String expectedStatement = "Account: AC001" + System.lineSeparator() +
                "| Date     | Txn Id      | Type |    Amount   |" + System.lineSeparator() +
                "| 20230929 | 20230929-05 | D    |      150.00 |" + System.lineSeparator() +
                "| 20230930 | 20230930-01 | D    |      150.00 |" + System.lineSeparator() +
                "| 20230930 | 20230930-02 | W    |       50.00 |" + System.lineSeparator();

        Assertions.assertEquals(expectedStatement.trim(), statement.trim());
    }

    @Test
    void testRetrieveStatementForAccount() throws IllegalAccessException {
        TransactionItem transactionItem0 = new TransactionItem("20230505-01", LocalDate.parse("20230505",
                DateTimeFormatter.BASIC_ISO_DATE), "AC001", TransactionItem.TransactionType.D, 100.00, 100.00);
        TransactionItem transactionItem1 = new TransactionItem("20230601-01", LocalDate.parse("20230601",
                DateTimeFormatter.BASIC_ISO_DATE), "AC001", TransactionItem.TransactionType.D, 150.00, 250.00);
        TransactionItem transactionItem2 = new TransactionItem("20230626-01", LocalDate.parse("20230626",
                DateTimeFormatter.BASIC_ISO_DATE), "AC001", TransactionItem.TransactionType.W, 20.00, 230.00);
        TransactionItem transactionItem3 = new TransactionItem("20230626-02", LocalDate.parse("20230626",
                DateTimeFormatter.BASIC_ISO_DATE), "AC001", TransactionItem.TransactionType.W, 100.00, 130.00);
        List<TransactionItem> transactionList = new ArrayList<>();
        transactionList.add(transactionItem0);
        transactionList.add(transactionItem1);
        transactionList.add(transactionItem2);
        transactionList.add(transactionItem3);

        TransactionsRepo transactionsRepo = getTransactionRepo(transactionList);

        InterestItem interestItem1 = new InterestItem(LocalDate.parse("20230101", DateTimeFormatter.BASIC_ISO_DATE), "RULE01", 1.95);
        InterestItem interestItem2 = new InterestItem(LocalDate.parse("20230520", DateTimeFormatter.BASIC_ISO_DATE), "RULE02", 1.90);
        InterestItem interestItem3 = new InterestItem(LocalDate.parse("20230615", DateTimeFormatter.BASIC_ISO_DATE), "RULE03", 2.20);
        List<InterestItem> interestItems = new ArrayList<>();
        interestItems.add(interestItem1);
        interestItems.add(interestItem2);
        interestItems.add(interestItem3);

        StatementService statementService = getStatementService(transactionsRepo,
                getInterestService(getInterestRepo(interestItems), transactionsRepo));

        String statement = statementService.retrieveStatementForAccount("AC001 202306");

        String expectedStatement = "Account: AC001" + System.lineSeparator() +
                "| Date     | Txn Id      | Type |    Amount   |   Balance   |" + System.lineSeparator() +
                "| 20230601 | 20230601-01 | D    |      150.00 |      250.00 |" + System.lineSeparator() +
                "| 20230626 | 20230626-01 | W    |       20.00 |      230.00 |" + System.lineSeparator() +
                "| 20230626 | 20230626-02 | W    |      100.00 |      130.00 |" + System.lineSeparator() +
                "| 20230630 |             | I    |        0.39 |      130.39 |" + System.lineSeparator();

        Assertions.assertEquals(expectedStatement.trim(), statement.trim());
    }
}
