package com.mlpj.simple.banking.system.service;

import com.mlpj.simple.banking.system.exception.InsufficientBalanceException;
import com.mlpj.simple.banking.system.exception.OlderTransactionDateException;
import com.mlpj.simple.banking.system.model.TransactionItem;
import com.mlpj.simple.banking.system.repo.TransactionsRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import static com.mlpj.simple.banking.system.TestUtils.getTransactionRepo;
import static com.mlpj.simple.banking.system.TestUtils.getTransactionsService;

class TransactionsServiceTest {

    @Test
    void testProcessTransaction() throws IllegalAccessException {
        List<TransactionItem> transactionList = new ArrayList<>();

        TransactionsRepo transactionsRepo = getTransactionRepo(transactionList);
        TransactionsService transactionsService = getTransactionsService(transactionsRepo);

        // initial withdraw
        Assertions.assertThrows(InsufficientBalanceException.class, () -> transactionsService.processTransaction("20230626 AC001 W 100.00"));

        // initial deposit
        TransactionItem transactionItem = transactionsService.processTransaction("20230626 AC001 D 100.00");
        Assertions.assertEquals(LocalDate.parse("20230626", DateTimeFormatter.BASIC_ISO_DATE), transactionItem.getDate());
        Assertions.assertEquals("20230626-01", transactionItem.getTransactionID());
        Assertions.assertEquals(TransactionItem.TransactionType.D, transactionItem.getTransactionType());
        Assertions.assertEquals(100.00, transactionItem.getBalance());
        Assertions.assertEquals("AC001", transactionItem.getAccountName());
        Assertions.assertEquals(100.00, transactionItem.getAmount());

        // withdraw
        TransactionItem transactionItem2 = transactionsService.processTransaction("20230626 AC001 W 25.00");
        Assertions.assertEquals(LocalDate.parse("20230626", DateTimeFormatter.BASIC_ISO_DATE), transactionItem2.getDate());
        Assertions.assertEquals("20230626-02", transactionItem2.getTransactionID());
        Assertions.assertEquals(TransactionItem.TransactionType.W, transactionItem2.getTransactionType());
        Assertions.assertEquals(75.00, transactionItem2.getBalance());
        Assertions.assertEquals("AC001", transactionItem2.getAccountName());
        Assertions.assertEquals(25.00, transactionItem2.getAmount());

        // withdraw limit exceed
        Assertions.assertThrows(InsufficientBalanceException.class, () -> transactionsService.processTransaction("20230626 AC001 W 76.00"));

        // initial deposit new account AC002
        TransactionItem transactionItem3 = transactionsService.processTransaction("20230626 AC002 D 200.00");
        Assertions.assertEquals(LocalDate.parse("20230626", DateTimeFormatter.BASIC_ISO_DATE), transactionItem3.getDate());
        Assertions.assertEquals("20230626-01", transactionItem3.getTransactionID());
        Assertions.assertEquals(TransactionItem.TransactionType.D, transactionItem3.getTransactionType());
        Assertions.assertEquals(200.00, transactionItem3.getBalance());
        Assertions.assertEquals("AC002", transactionItem3.getAccountName());
        Assertions.assertEquals(200.00, transactionItem3.getAmount());

        // withdraw AC002
        TransactionItem transactionItem4 = transactionsService.processTransaction("20230626 AC002 W 50.00");
        Assertions.assertEquals(LocalDate.parse("20230626", DateTimeFormatter.BASIC_ISO_DATE), transactionItem4.getDate());
        Assertions.assertEquals("20230626-02", transactionItem4.getTransactionID());
        Assertions.assertEquals(TransactionItem.TransactionType.W, transactionItem4.getTransactionType());
        Assertions.assertEquals(150.00, transactionItem4.getBalance());
        Assertions.assertEquals("AC002", transactionItem4.getAccountName());
        Assertions.assertEquals(50.00, transactionItem4.getAmount());

        // withdraw AC002 new date
        TransactionItem transactionItem5 = transactionsService.processTransaction("20230627 AC002 W 30.00");
        Assertions.assertEquals(LocalDate.parse("20230627", DateTimeFormatter.BASIC_ISO_DATE), transactionItem5.getDate());
        Assertions.assertEquals("20230627-01", transactionItem5.getTransactionID());
        Assertions.assertEquals(TransactionItem.TransactionType.W, transactionItem5.getTransactionType());
        Assertions.assertEquals(120.00, transactionItem5.getBalance());
        Assertions.assertEquals("AC002", transactionItem5.getAccountName());
        Assertions.assertEquals(30.00, transactionItem5.getAmount());

        // deposit AC002
        TransactionItem transactionItem6 = transactionsService.processTransaction("20230627 AC002 D 10.00");
        Assertions.assertEquals(LocalDate.parse("20230627", DateTimeFormatter.BASIC_ISO_DATE), transactionItem6.getDate());
        Assertions.assertEquals("20230627-02", transactionItem6.getTransactionID());
        Assertions.assertEquals(TransactionItem.TransactionType.D, transactionItem6.getTransactionType());
        Assertions.assertEquals(130.00, transactionItem6.getBalance());
        Assertions.assertEquals("AC002", transactionItem6.getAccountName());
        Assertions.assertEquals(10.00, transactionItem6.getAmount());

        // withdraw AC002 old date
        Assertions.assertThrows(OlderTransactionDateException.class, () -> transactionsService.processTransaction("20230626 AC002 W 30.00"));

        // transaction with invalid date format
        Assertions.assertThrows(DateTimeParseException.class, () -> transactionsService.processTransaction("2023-06-26 AC002 W 30.00"));
    }
}
