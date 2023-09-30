package com.mlpj.simple.banking.system;

import com.mlpj.simple.banking.system.model.TransactionItem;
import com.mlpj.simple.banking.system.repo.TransactionsRepo;
import com.mlpj.simple.banking.system.service.StatementService;
import com.mlpj.simple.banking.system.service.TransactionsService;
import org.junit.platform.commons.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;

public class TestUtils {

    public static TransactionsRepo getTransactionRepo(List<TransactionItem> transactionList) throws IllegalAccessException {
        TransactionsRepo transactionsRepo = new TransactionsRepo();

        Field field = ReflectionUtils
                .findFields(TransactionsRepo.class, f -> f.getName().equals("transactionList"),
                        ReflectionUtils.HierarchyTraversalMode.TOP_DOWN)
                .get(0);

        field.setAccessible(true);
        field.set(transactionsRepo, transactionList);

        return transactionsRepo;
    }

    public static TransactionsService getTransactionsService(TransactionsRepo transactionsRepo) throws IllegalAccessException {
        TransactionsService transactionsService = new TransactionsService();

        Field field = ReflectionUtils
                .findFields(TransactionsService.class, f -> f.getName().equals("transactionsRepo"),
                        ReflectionUtils.HierarchyTraversalMode.TOP_DOWN)
                .get(0);

        field.setAccessible(true);
        field.set(transactionsService, transactionsRepo);

        return transactionsService;
    }

    public static StatementService getStatementService(TransactionsRepo transactionsRepo) throws IllegalAccessException {
        StatementService statementService = new StatementService();

        Field field = ReflectionUtils
                .findFields(StatementService.class, f -> f.getName().equals("transactionsRepo"),
                        ReflectionUtils.HierarchyTraversalMode.TOP_DOWN)
                .get(0);

        field.setAccessible(true);
        field.set(statementService, transactionsRepo);

        return statementService;
    }
}