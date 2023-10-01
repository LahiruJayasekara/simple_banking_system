package com.mlpj.simple.banking.system.model;

import java.time.LocalDate;

public class TransactionItem {

    private String transactionID;
    private LocalDate date;
    private String accountName;
    private TransactionType transactionType;
    private double amount;
    private double balance;

    public enum TransactionType {
        W, //withdraw
        D, //deposit
        I  //interest
    }

    public TransactionItem(String transactionID, LocalDate date, String accountName, TransactionType transactionType, double amount, double balance) {
        this.transactionID = transactionID;
        this.date = date;
        this.accountName = accountName;
        this.transactionType = transactionType;
        this.amount = amount;
        this.balance = balance;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public TransactionItem clone() {
        return new TransactionItem(transactionID, date, accountName, transactionType, amount, balance);
    }
}
