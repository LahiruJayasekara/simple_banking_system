package com.mlpj.simple.banking.system.exception;

public class OlderTransactionDateException extends RuntimeException {
    public OlderTransactionDateException(String accountName) {
        super(String.format("Older transaction for account : %s", accountName));
    }
}
