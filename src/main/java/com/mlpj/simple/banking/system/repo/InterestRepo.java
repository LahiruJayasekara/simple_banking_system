package com.mlpj.simple.banking.system.repo;

import com.mlpj.simple.banking.system.model.InterestItem;

import java.util.ArrayList;
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
        return interestItems;
    }
}
