package com.mlpj.simple.banking.system.service;

import com.mlpj.simple.banking.system.exception.InvalidInterestException;
import com.mlpj.simple.banking.system.model.InterestItem;
import com.mlpj.simple.banking.system.repo.InterestRepo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

import static com.mlpj.simple.banking.system.util.Constants.INTEREST_STATEMENT_HEADERS;

public class InterestService {

    private InterestRepo interestRepo = new InterestRepo();

    public InterestItem processInterestDefinition(String interestDefinitionString) throws NumberFormatException {
        String[] interestInfo = interestDefinitionString.split(" ");
        LocalDate interestDate = LocalDate.parse(interestInfo[0], DateTimeFormatter.BASIC_ISO_DATE);
        String interestRuleId = interestInfo[1];
        double interestRate = Double.parseDouble(interestInfo[2]);

        if (interestRate >= 100 || interestRate <= 0) {
            throw new InvalidInterestException(interestRuleId);
        }

        InterestItem newInterestItem = new InterestItem(interestDate, interestRuleId, interestRate);
        interestRepo.addInterest(newInterestItem);
        return newInterestItem;
    }

    public String getInterestListStatement() {
        List<InterestItem> allInterestItems = interestRepo.getAllInterestItems();
        Stream<String> formattedInterestList = allInterestItems.stream().map(this::interestToFormattedSting);

        StringBuilder statement = new StringBuilder();
        statement.append("Interest rules:").append(System.lineSeparator());
        statement.append(INTEREST_STATEMENT_HEADERS).append(System.lineSeparator());

        formattedInterestList.forEach(s -> statement.append(s).append(System.lineSeparator()));
        return statement.toString();
    }

    private String interestToFormattedSting(InterestItem interestItem) {
        return String.format("| %s | %-20s | %7.2f |", interestItem.getDate().format(DateTimeFormatter.BASIC_ISO_DATE), interestItem.getRuleId(),
                interestItem.getRate());
    }
}
