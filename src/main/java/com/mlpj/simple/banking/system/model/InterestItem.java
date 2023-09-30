package com.mlpj.simple.banking.system.model;

import java.time.LocalDate;

public class InterestItem {
    private LocalDate date;
    private String ruleId;
    private double rate;

    public InterestItem(LocalDate date, String ruleId, double rate) {
        this.date = date;
        this.ruleId = ruleId;
        this.rate = rate;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof InterestItem)) {
            return false;
        }

        return ((InterestItem) obj).getDate().equals(date);
    }
}
