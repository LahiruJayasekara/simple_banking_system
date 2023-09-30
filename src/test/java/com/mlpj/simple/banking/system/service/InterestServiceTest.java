package com.mlpj.simple.banking.system.service;

import com.mlpj.simple.banking.system.exception.InvalidInterestException;
import com.mlpj.simple.banking.system.model.InterestItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static com.mlpj.simple.banking.system.TestUtils.getInterestRepo;
import static com.mlpj.simple.banking.system.TestUtils.getInterestService;

class InterestServiceTest {

    @Test
    void testProcessInterestDefinition() throws IllegalAccessException {
        InterestService interestService = getInterestService(getInterestRepo(new ArrayList<>()));

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
        InterestService interestService = getInterestService(getInterestRepo(new ArrayList<>()));
        interestService.processInterestDefinition("20230615 RULE01 2.20");
        interestService.processInterestDefinition("20230715 RULE02 1.20");
        interestService.processInterestDefinition("20230715 RULE02 5.20");

        String interestListStatement = interestService.getInterestListStatement();
        Assertions.assertEquals("Interest rules:" + System.lineSeparator() +
                "| Date     | RuleId               | Rate(%) |" + System.lineSeparator() +
                "| 20230615 | RULE01               |    2.20 |" + System.lineSeparator() +
                "| 20230715 | RULE02               |    5.20 |", interestListStatement.trim());
    }
}
