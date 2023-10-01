package com.mlpj.simple.banking.system.repo;

import com.mlpj.simple.banking.system.model.TransactionItem;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;


public class TransactionsRepo {

    private static List<TransactionItem> transactionList = new ArrayList<>();

    public List<TransactionItem> getTransactionsForAccount(String accountName) {
        return transactionList.stream().filter(transactionItem -> transactionItem.getAccountName().equals(accountName))
                .toList();
    }

    public List<TransactionItem> getTransactionsForMonth(String accountName, YearMonth yearMonth) {
        return transactionList.stream().filter(transactionItem -> transactionItem.getAccountName().equals(accountName))
                .filter(transactionItem -> transactionItem.getDate().getYear() == yearMonth.getYear()
                        && transactionItem.getDate().getMonth().equals(yearMonth.getMonth()))
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

    public List<TransactionItem> getTransactionListForMonthAccumulatedByDate(String accName, YearMonth yearMonth) {
        Map<LocalDate, List<TransactionItem>> groupedByDate = transactionList.stream()
                .filter(transactionItem -> transactionItem.getAccountName().equals(accName))
                .filter(transactionItem -> transactionItem.getDate().getYear() == yearMonth.getYear()
                        && transactionItem.getDate().getMonth().equals(yearMonth.getMonth()))
                .collect(Collectors.groupingBy(TransactionItem::getDate));

        return groupedByDate.values().stream()
                .map(transactionItems -> {
                    TransactionItem firstTransactionItem = transactionItems.get(0);
                    TransactionItem lastTransactionItem = transactionItems.get(transactionItems.size() - 1);
                    double aggregatedTransactionAmount = (firstTransactionItem.getTransactionType().equals(TransactionItem.TransactionType.D)
                            ? firstTransactionItem.getAmount() : -1 * firstTransactionItem.getAmount())
                            + lastTransactionItem.getBalance() - firstTransactionItem.getBalance();
                    lastTransactionItem.setTransactionType(aggregatedTransactionAmount < 0 ? TransactionItem.TransactionType.W : TransactionItem.TransactionType.D);
                    lastTransactionItem.setAmount(aggregatedTransactionAmount < 0 ? -1 * aggregatedTransactionAmount : aggregatedTransactionAmount);
                    return lastTransactionItem;
                })
                .sorted(Comparator.comparing(TransactionItem::getDate))
                .toList();
    }

    public TransactionItem getPreviousTransactionForTheMonth(String accName, YearMonth yearMonth) {
        TransactionItem transaction = null;
        List<TransactionItem> transactionsForAccount = getTransactionsForAccount(accName);
        for (TransactionItem currTransaction : transactionsForAccount) {
            if (currTransaction.getDate().getYear() == yearMonth.getYear() && currTransaction.getDate().getMonth().equals(yearMonth.getMonth())) {
                return transaction;
            } else {
                transaction = currTransaction;
            }
        }
        return null;
    }

    public void addTransaction(TransactionItem transactionItem) {
        transactionList.add(transactionItem);
    }
}
