package com.mlpj.simple.banking.system.service;

import com.mlpj.simple.banking.system.exception.InsufficientBalanceException;
import com.mlpj.simple.banking.system.exception.OlderTransactionDateException;
import com.mlpj.simple.banking.system.model.TransactionItem;
import com.mlpj.simple.banking.system.repo.TransactionsRepo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TransactionsService {

    private final TransactionsRepo transactionsRepo = new TransactionsRepo();

    public TransactionItem processTransaction(String transactionString) throws DateTimeParseException, NumberFormatException {
        String[] transactionInfo = transactionString.split(" ");
        LocalDate transactionDate = LocalDate.parse(transactionInfo[0], DateTimeFormatter.BASIC_ISO_DATE);
        String accountName = transactionInfo[1];
        TransactionItem.TransactionType transactionType = transactionInfo[2].equalsIgnoreCase("W") ?
                TransactionItem.TransactionType.W : TransactionItem.TransactionType.D;
        double amount = Double.parseDouble(transactionInfo[3]);
        String transactionID = String.format("%s-%02d", transactionInfo[0], transactionsRepo.getNextTransactionID(transactionDate, accountName));

        TransactionItem lastTransaction = transactionsRepo.getLastTransaction(accountName);
        double newBalance;
        if (lastTransaction != null) {
            if (transactionDate.isBefore(lastTransaction.getDate())) {
                throw new OlderTransactionDateException(accountName);
            }

            newBalance = transactionType.equals(TransactionItem.TransactionType.W) ? lastTransaction.getBalance() - amount
                    : lastTransaction.getBalance() + amount;

        } else {
            if (transactionType.equals(TransactionItem.TransactionType.W)) {
                throw new InsufficientBalanceException(accountName);
            }
            newBalance = amount;
        }

        if (newBalance < 0) {
            throw new InsufficientBalanceException(accountName);
        }

        TransactionItem newTransaction = new TransactionItem(transactionID, transactionDate, accountName, transactionType, amount, newBalance);

        transactionsRepo.addTransaction(newTransaction);

        return newTransaction;
    }
}
