package com.mlpj.simple.banking.system.repo;

import com.mlpj.simple.banking.system.model.InterestItem;
import com.mlpj.simple.banking.system.model.TransactionItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.mlpj.simple.banking.system.TestUtils.getInterestRepo;
import static com.mlpj.simple.banking.system.TestUtils.getTransactionRepo;

class InterestRepoTest {

    @Test
    void testAddInterest() throws IllegalAccessException {

        InterestRepo interestRepo = getInterestRepo(new ArrayList<>());

        // initial interest
        InterestItem interestItem1 = new InterestItem(LocalDate.parse("20230101", DateTimeFormatter.BASIC_ISO_DATE), "RULE01", 1.95);
        Assertions.assertDoesNotThrow(() -> interestRepo.addInterest(interestItem1));

        // 2nd interest
        InterestItem interestItem2 = new InterestItem(LocalDate.parse("20230520", DateTimeFormatter.BASIC_ISO_DATE), "RULE02", 1.90);
        Assertions.assertDoesNotThrow(() -> interestRepo.addInterest(interestItem2));

        // replace 1st interest
        InterestItem interestItem3 = new InterestItem(LocalDate.parse("20230101", DateTimeFormatter.BASIC_ISO_DATE), "RULE01", 2.20);
        Assertions.assertDoesNotThrow(() -> interestRepo.addInterest(interestItem3));

        List<InterestItem> allInterestItems = interestRepo.getAllInterestItems();
        Assertions.assertEquals(2, allInterestItems.size());
        Assertions.assertEquals("RULE01", allInterestItems.get(0).getRuleId());
        Assertions.assertEquals(2.20, allInterestItems.get(0).getRate());
        Assertions.assertEquals("RULE02", allInterestItems.get(1).getRuleId());
        Assertions.assertEquals(1.90, allInterestItems.get(1).getRate());
    }

}
