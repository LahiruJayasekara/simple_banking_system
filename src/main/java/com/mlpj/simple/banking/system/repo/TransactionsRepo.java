package com.mlpj.simple.banking.system.repo;

import com.mlpj.simple.banking.system.model.TransactionItem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class TransactionsRepo {

    private static List<TransactionItem> transactionList = new ArrayList<>();

    public List<TransactionItem> getTransactionsForAccount(String accountName) {
        return transactionList.stream().filter(transactionItem -> transactionItem.getAccountName().equals(accountName))
                .toList();
    }

    public int getNextTransactionID(LocalDate date, String accountName) {
        List<Integer> idList = transactionList.stream()
                .filter(transactionItem -> transactionItem.getAccountName().equals(accountName) && transactionItem.getDate().equals(date))
                .map(transactionItem -> Integer.parseInt(transactionItem.getTransactionID().split("-")[1])).toList();

        return idList.isEmpty() ? 1 : Collections.max(idList) + 1;
    }

    //not sorting, since new transactions are with older dates are not allowed
    public TransactionItem getLastTransaction(String accountName) {
        List<TransactionItem> transactionItems = transactionList.stream()
                .filter(transactionItem -> transactionItem.getAccountName().equals(accountName))
                .toList();
        return transactionItems.isEmpty() ? null : transactionItems.get(transactionItems.size() - 1);
    }

    public void addTransaction(TransactionItem transactionItem) {
        transactionList.add(transactionItem);
    }
}
