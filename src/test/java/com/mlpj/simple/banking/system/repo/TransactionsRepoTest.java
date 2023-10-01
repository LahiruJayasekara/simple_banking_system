package com.mlpj.simple.banking.system.repo;

import com.mlpj.simple.banking.system.model.TransactionItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.mlpj.simple.banking.system.TestUtils.getTransactionRepo;

class TransactionsRepoTest {

    @Test
    void testGetTransactionsForAccount() throws IllegalAccessException {
        TransactionItem transactionItem1 = new TransactionItem("20230930-01", LocalDate.parse("20230930",
                DateTimeFormatter.BASIC_ISO_DATE), "AC001", TransactionItem.TransactionType.D, 150.00, 150.00);
        TransactionItem transactionItem2 = new TransactionItem("20230930-02", LocalDate.parse("20230930",
                DateTimeFormatter.BASIC_ISO_DATE), "AC001", TransactionItem.TransactionType.W, 50.00, 100.00);
        List<TransactionItem> transactionList = new ArrayList<>();
        transactionList.add(transactionItem1);
        transactionList.add(transactionItem2);

        TransactionsRepo transactionsRepo = getTransactionRepo(transactionList);
        List<TransactionItem> transactions = transactionsRepo.getTransactionsForAccount("AC001");
        Assertions.assertEquals(2, transactions.size());
        Assertions.assertEquals(transactionItem1, transactions.get(0));
        Assertions.assertEquals(transactionItem2, transactions.get(1));
    }

    @Test
    void testGetEmptyTransactionsForAccount() throws IllegalAccessException {
        List<TransactionItem> transactionList = new ArrayList<>();

        TransactionsRepo transactionsRepo = getTransactionRepo(transactionList);
        List<TransactionItem> transactions = transactionsRepo.getTransactionsForAccount("AC001");
        Assertions.assertEquals(0, transactions.size());
    }

    @Test
    void testGetNextTransactionID() throws IllegalAccessException {
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
        int nextTransactionID = transactionsRepo.getNextTransactionID(LocalDate.parse("20230930", DateTimeFormatter.BASIC_ISO_DATE),
                "AC001");
        Assertions.assertEquals(3, nextTransactionID);
    }

    @Test
    void testGetInitialNextTransactionID() throws IllegalAccessException {
        List<TransactionItem> transactionList = new ArrayList<>();

        TransactionsRepo transactionsRepo = getTransactionRepo(transactionList);
        int nextTransactionID = transactionsRepo.getNextTransactionID(LocalDate.parse("20230930", DateTimeFormatter.BASIC_ISO_DATE),
                "AC001");
        Assertions.assertEquals(1, nextTransactionID);
    }

    @Test
    void testGetLastTransaction() throws IllegalAccessException {
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
        TransactionItem lastTransaction = transactionsRepo.getLastTransaction("AC001");
        Assertions.assertEquals(transactionItem2, lastTransaction);
    }

    @Test
    void testGetLastTransactionForEmptyTransactionList() throws IllegalAccessException {
        List<TransactionItem> transactionList = new ArrayList<>();

        TransactionsRepo transactionsRepo = getTransactionRepo(transactionList);
        TransactionItem lastTransaction = transactionsRepo.getLastTransaction("AC001");
        Assertions.assertNull(lastTransaction);
    }
}
