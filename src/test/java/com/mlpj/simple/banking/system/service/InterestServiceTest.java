package com.mlpj.simple.banking.system.service;

import com.mlpj.simple.banking.system.exception.InvalidInterestException;
import com.mlpj.simple.banking.system.model.InterestItem;
import com.mlpj.simple.banking.system.model.TransactionItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.mlpj.simple.banking.system.TestUtils.*;

class InterestServiceTest {

    @Test
    void testProcessInterestDefinition() throws IllegalAccessException {
        InterestService interestService = getInterestService(getInterestRepo(new ArrayList<>()), getTransactionRepo(new ArrayList<>()));

        // initial interest
        InterestItem interestItem = interestService.processInterestDefinition("20230615 RULE01 2.20");
        Assertions.assertEquals(LocalDate.parse("20230615", DateTimeFormatter.BASIC_ISO_DATE), interestItem.getDate());
        Assertions.assertEquals("RULE01", interestItem.getRuleId());
        Assertions.assertEquals(2.20, interestItem.getRate());

        // invalid rate
        Assertions.assertThrows(InvalidInterestException.class, () -> interestService.processInterestDefinition("20230615 RULE01 0.00"));
        Assertions.assertThrows(InvalidInterestException.class, () -> interestService.processInterestDefinition("20230615 RULE01 100.00"));
        Assertions.assertThrows(InvalidInterestException.class, () -> interestService.processInterestDefinition("20230615 RULE01 101.00"));
        Assertions.assertThrows(InvalidInterestException.class, () -> interestService.processInterestDefinition("20230615 RULE01 -1.00"));

        // second interest
        InterestItem interestItem1 = interestService.processInterestDefinition("20230715 RULE02 1.20");
        Assertions.assertEquals(LocalDate.parse("20230715", DateTimeFormatter.BASIC_ISO_DATE), interestItem1.getDate());
        Assertions.assertEquals("RULE02", interestItem1.getRuleId());
        Assertions.assertEquals(1.20, interestItem1.getRate());

        // override second interest
        InterestItem interestItem2 = interestService.processInterestDefinition("20230715 RULE02 5.20");
        Assertions.assertEquals(LocalDate.parse("20230715", DateTimeFormatter.BASIC_ISO_DATE), interestItem2.getDate());
        Assertions.assertEquals("RULE02", interestItem2.getRuleId());
        Assertions.assertEquals(5.20, interestItem2.getRate());
    }

    @Test
    void testGetInterestListStatement() throws IllegalAccessException {
        InterestService interestService = getInterestService(getInterestRepo(new ArrayList<>()), getTransactionRepo(new ArrayList<>()));
        interestService.processInterestDefinition("20230615 RULE01 2.20");
        interestService.processInterestDefinition("20230715 RULE02 1.20");
        interestService.processInterestDefinition("20230715 RULE02 5.20");

        String interestListStatement = interestService.getInterestListStatement();
        Assertions.assertEquals("Interest rules:" + System.lineSeparator() +
                "| Date     | RuleId               | Rate(%) |" + System.lineSeparator() +
                "| 20230615 | RULE01               |    2.20 |" + System.lineSeparator() +
                "| 20230715 | RULE02               |    5.20 |", interestListStatement.trim());
    }

    @Test
    void calculateInterestForMonthWithTransactionInBeginning() throws IllegalAccessException {
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

        InterestItem interestItem1 = new InterestItem(LocalDate.parse("20230101", DateTimeFormatter.BASIC_ISO_DATE), "RULE01", 1.95);
        InterestItem interestItem2 = new InterestItem(LocalDate.parse("20230520", DateTimeFormatter.BASIC_ISO_DATE), "RULE02", 1.90);
        InterestItem interestItem3 = new InterestItem(LocalDate.parse("20230615", DateTimeFormatter.BASIC_ISO_DATE), "RULE03", 2.20);
        List<InterestItem> interestItems = new ArrayList<>();
        interestItems.add(interestItem1);
        interestItems.add(interestItem2);
        interestItems.add(interestItem3);

        InterestService interestService = getInterestService(getInterestRepo(interestItems), getTransactionRepo(transactionList));

        TransactionItem interestTransaction = interestService.calculateInterestForMonth("AC001", "202306");
        Assertions.assertNull(interestTransaction.getTransactionID());
        Assertions.assertEquals(LocalDate.parse("20230630", DateTimeFormatter.BASIC_ISO_DATE), interestTransaction.getDate());
        Assertions.assertEquals(0.39, interestTransaction.getAmount());
        Assertions.assertEquals(130.39, interestTransaction.getBalance());
        Assertions.assertEquals(TransactionItem.TransactionType.I, interestTransaction.getTransactionType());
        Assertions.assertEquals("AC001", interestTransaction.getAccountName());
    }

    @Test
    void calculateInterestForMonth() throws IllegalAccessException {
        TransactionItem transactionItem0 = new TransactionItem("20230505-01", LocalDate.parse("20230505",
                DateTimeFormatter.BASIC_ISO_DATE), "AC001", TransactionItem.TransactionType.D, 100.00, 100.00);
        TransactionItem transactionItem1 = new TransactionItem("20230605-01", LocalDate.parse("20230605",
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

        InterestItem interestItem1 = new InterestItem(LocalDate.parse("20230101", DateTimeFormatter.BASIC_ISO_DATE), "RULE01", 1.95);
        InterestItem interestItem2 = new InterestItem(LocalDate.parse("20230520", DateTimeFormatter.BASIC_ISO_DATE), "RULE02", 1.90);
        InterestItem interestItem3 = new InterestItem(LocalDate.parse("20230615", DateTimeFormatter.BASIC_ISO_DATE), "RULE03", 2.20);
        List<InterestItem> interestItems = new ArrayList<>();
        interestItems.add(interestItem1);
        interestItems.add(interestItem2);
        interestItems.add(interestItem3);

        InterestService interestService = getInterestService(getInterestRepo(interestItems), getTransactionRepo(transactionList));

        TransactionItem interestTransaction = interestService.calculateInterestForMonth("AC001", "202306");
        Assertions.assertNull(interestTransaction.getTransactionID());
        Assertions.assertEquals(LocalDate.parse("20230630", DateTimeFormatter.BASIC_ISO_DATE), interestTransaction.getDate());
        Assertions.assertEquals(0.36, interestTransaction.getAmount());
        Assertions.assertEquals(130.36, interestTransaction.getBalance());
        Assertions.assertEquals(TransactionItem.TransactionType.I, interestTransaction.getTransactionType());
        Assertions.assertEquals("AC001", interestTransaction.getAccountName());
    }
}
