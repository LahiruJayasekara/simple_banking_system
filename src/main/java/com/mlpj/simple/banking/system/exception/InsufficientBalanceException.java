package com.mlpj.simple.banking.system.exception;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String accountName) {
        super(String.format("Insufficient balance for account : %s", accountName));
    }
}
