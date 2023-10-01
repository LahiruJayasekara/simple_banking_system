package com.mlpj.simple.banking.system.repo;

import com.mlpj.simple.banking.system.model.InterestItem;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class InterestRepo {
    private static List<InterestItem> interestItems = new ArrayList<>();


    public void addInterest(InterestItem interestItem) {
        // equals method of InterestItem overridden to match by date
        if (interestItems.contains(interestItem)) {
            int index = interestItems.lastIndexOf(interestItem);
            interestItems.set(index, interestItem);
        } else {
            interestItems.add(interestItem);
        }
    }

    public List<InterestItem> getAllInterestItems() {
        List<InterestItem> interestItemsClone = new ArrayList<>(interestItems);
        interestItemsClone.sort(Comparator.comparing(InterestItem::getDate));
        return interestItemsClone;
    }

    public InterestItem getLastMonthInterest(YearMonth yearMonth) {
        List<InterestItem> interestItemsClone = new ArrayList<>(interestItems);
        interestItemsClone.sort(Comparator.comparing(InterestItem::getDate));

        //set initial interest to 0
        InterestItem lastInterestItem = new InterestItem(LocalDate.parse("00000101", DateTimeFormatter.BASIC_ISO_DATE), "DEFAULT_INTEREST", 0.0);

        for (InterestItem interestItem : interestItemsClone) {
            if (interestItem.getDate().getYear() == yearMonth.getYear() && interestItem.getDate().getMonth().equals(yearMonth.getMonth())) {
                return lastInterestItem;
            }
            lastInterestItem = interestItem;
        }
        return lastInterestItem;
    }
}
