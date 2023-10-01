package com.mlpj.simple.banking.system.exception;

public class InvalidInterestException extends RuntimeException {
    public InvalidInterestException(String interestRuleId) {
        super(String.format("Invalid interest for rule id : %s", interestRuleId));
    }
}
