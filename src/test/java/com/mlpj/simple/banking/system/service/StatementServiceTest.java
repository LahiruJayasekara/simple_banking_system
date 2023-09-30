package com.mlpj.simple.banking.system.service;

import com.mlpj.simple.banking.system.model.TransactionItem;
import com.mlpj.simple.banking.system.repo.TransactionsRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.mlpj.simple.banking.system.TestUtils.getStatementService;
import static com.mlpj.simple.banking.system.TestUtils.getTransactionRepo;

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
        StatementService statementService = getStatementService(transactionsRepo);

        String statement = statementService.retrieveStatementForAccountWithoutBalance("AC001");

        String expectedStatement = "Account: AC001" + System.lineSeparator() +
                "| Date     | Txn Id      | Type |    Amount   |" + System.lineSeparator() +
                "| 20230929 | 20230929-05 | D    |      150.00 |" + System.lineSeparator() +
                "| 20230930 | 20230930-01 | D    |      150.00 |" + System.lineSeparator() +
                "| 20230930 | 20230930-02 | W    |       50.00 |" + System.lineSeparator();

        Assertions.assertEquals(expectedStatement.trim(), statement.trim());
    }
}
