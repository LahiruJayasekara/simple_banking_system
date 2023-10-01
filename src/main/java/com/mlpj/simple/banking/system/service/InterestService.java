package com.mlpj.simple.banking.system.service;

import com.mlpj.simple.banking.system.exception.InvalidInterestException;
import com.mlpj.simple.banking.system.model.InterestItem;
import com.mlpj.simple.banking.system.model.TransactionItem;
import com.mlpj.simple.banking.system.repo.InterestRepo;
import com.mlpj.simple.banking.system.repo.TransactionsRepo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.mlpj.simple.banking.system.util.Constants.INTEREST_STATEMENT_HEADERS;

public class InterestService {

    private InterestRepo interestRepo = new InterestRepo();
    private TransactionsRepo transactionsRepo = new TransactionsRepo();

    public InterestItem processInterestDefinition(String interestDefinitionString) throws NumberFormatException {
        String[] interestInfo = interestDefinitionString.split(" ");
        LocalDate interestDate = LocalDate.parse(interestInfo[0], DateTimeFormatter.BASIC_ISO_DATE);
        String interestRuleId = interestInfo[1];
        double interestRate = Double.parseDouble(interestInfo[2]);

        if (interestRate >= 100 || interestRate <= 0) {
            throw new InvalidInterestException(interestRuleId);
        }

        InterestItem newInterestItem = new InterestItem(interestDate, interestRuleId, interestRate);
        interestRepo.addInterest(newInterestItem);
        return newInterestItem;
    }

    public String getInterestListStatement() {
        List<InterestItem> allInterestItems = interestRepo.getAllInterestItems();
        Stream<String> formattedInterestList = allInterestItems.stream().map(this::interestToFormattedSting);

        StringBuilder statement = new StringBuilder();
        statement.append("Interest rules:").append(System.lineSeparator());
        statement.append(INTEREST_STATEMENT_HEADERS).append(System.lineSeparator());

        formattedInterestList.forEach(s -> statement.append(s).append(System.lineSeparator()));
        return statement.toString();
    }

    public TransactionItem calculateInterestForMonth(String accName, String date) {
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyyMM");
        YearMonth yearMonth = YearMonth.parse(date, inputFormat);

        List<TransactionItem> transactionListForMonth = getTransactionItemListForMonth(accName, date, yearMonth);

        if (transactionListForMonth == null) {
            return null;
        }

        List<InterestItem> interestItemsForTheMonth = getInterestItemsForMonth(date, yearMonth);

        //adding a reference month end interest
        InterestItem monthEndInterest = interestItemsForTheMonth.get(interestItemsForTheMonth.size() - 1).clone();
        LocalDate lastDayOfMonth = monthEndInterest.getDate().with(TemporalAdjusters.lastDayOfMonth());
        if (monthEndInterest.getDate().getDayOfMonth() != lastDayOfMonth.getDayOfMonth()) {
            monthEndInterest.setDate(lastDayOfMonth);
            interestItemsForTheMonth.add(monthEndInterest);
        }

        List<LocalDate> datePointList = getDatePointList(transactionListForMonth, interestItemsForTheMonth);

        double interest = 0;
        for (int i = 1; i < datePointList.size(); i++) {
            interest = processDateInterval(transactionListForMonth, interestItemsForTheMonth, datePointList, interest, i);
        }

        double interestForTheMonth = interest / 365;
        BigDecimal bigDecimal = BigDecimal.valueOf(interestForTheMonth);
        BigDecimal roundedInterest = bigDecimal.setScale(2, RoundingMode.HALF_EVEN);
        double finalBalance = transactionListForMonth.get(transactionListForMonth.size() - 1).getBalance() + roundedInterest.doubleValue();
        return new TransactionItem(null, lastDayOfMonth, accName,
                TransactionItem.TransactionType.I, roundedInterest.doubleValue(), finalBalance);
    }

    private double processDateInterval(List<TransactionItem> transactionListForMonth, List<InterestItem> interestItemsForTheMonth, List<LocalDate> datePointList, double interest, int i) {
        LocalDate intervalStartDate = datePointList.get(i - 1);
        LocalDate intervalEndDate = datePointList.get(i);

        Optional<TransactionItem> transactionItemOptional = transactionListForMonth.stream().filter(t -> t.getDate().equals(intervalStartDate)).findFirst();

        if (transactionItemOptional.isPresent()) {
            TransactionItem transactionItem = transactionItemOptional.get();

            for (int j = i - 1; j >= 0; j--) {
                LocalDate d = datePointList.get(j);
                Optional<InterestItem> interestItemOptional = interestItemsForTheMonth.stream().filter(interestItem -> interestItem.getDate().equals(d)).findFirst();
                if (interestItemOptional.isPresent()) {
                    InterestItem interestItem = interestItemOptional.get();
                    int numberOfDays = getNumberOfDays(intervalEndDate, intervalStartDate, datePointList);
                    interest = calculateInterest(interest, numberOfDays, transactionItem, interestItem);
                    break;
                }
            }
        } else {
            InterestItem interestItem = interestItemsForTheMonth.stream()
                    .filter(interestItem1 -> interestItem1.getDate().equals(intervalStartDate)).toList().get(0);

            for (int j = i - 1; j >= 0; j--) {
                LocalDate d = datePointList.get(j);
                Optional<TransactionItem> transOptional = transactionListForMonth.stream().filter(t -> t.getDate().equals(d)).findFirst();
                if (transOptional.isPresent()) {
                    TransactionItem transactionItem = transOptional.get();
                    int numberOfDays = getNumberOfDays(intervalEndDate, intervalStartDate, datePointList);
                    interest = calculateInterest(interest, numberOfDays, transactionItem, interestItem);
                    break;
                }
            }
        }
        return interest;
    }

    private double calculateInterest(double interest, int numberOfDays, TransactionItem transactionItem, InterestItem interestItem) {
        interest += (numberOfDays * transactionItem.getBalance() * interestItem.getRate() / 100);
        return interest;
    }

    private int getNumberOfDays(LocalDate intervalEndDate, LocalDate intervalStartDate, List<LocalDate> datePointList) {
        int numberOfDays = intervalEndDate.getDayOfMonth() - intervalStartDate.getDayOfMonth();
        if (intervalEndDate.equals(datePointList.get(datePointList.size() - 1))) {
            numberOfDays++; //to consider the last day of month for interest calculation
        }
        return numberOfDays;
    }

    private List<LocalDate> getDatePointList(List<TransactionItem> transactionListForMonth, List<InterestItem> interestItemsForTheMonth) {
        List<LocalDate> datePointList = transactionListForMonth.stream().map(TransactionItem::getDate).collect(Collectors.toList());

        interestItemsForTheMonth.forEach(interestItem -> {
            if (!datePointList.contains(interestItem.getDate())) {
                datePointList.add(interestItem.getDate());
            }
        });

        datePointList.sort(LocalDate::compareTo);
        return datePointList;
    }

    private List<InterestItem> getInterestItemsForMonth(String date, YearMonth yearMonth) {
        List<InterestItem> allInterestItems = interestRepo.getAllInterestItems();
        List<InterestItem> interestItemsForTheMonth = allInterestItems.stream()
                .filter(interestItem -> interestItem.getDate().getYear() == yearMonth.getYear()
                        && interestItem.getDate().getMonth().equals(yearMonth.getMonth())).collect(Collectors.toList());

        //adding last month interest
        InterestItem lastMonthInterest = interestRepo.getLastMonthInterest(yearMonth);
        if (interestItemsForTheMonth.isEmpty() || interestItemsForTheMonth.get(0).getDate().getDayOfMonth() != 1) {
            lastMonthInterest.setDate(LocalDate.parse(date + "01", DateTimeFormatter.BASIC_ISO_DATE)); //modify date to start of month
            interestItemsForTheMonth.add(0, lastMonthInterest);
        }
        return interestItemsForTheMonth;
    }

    private List<TransactionItem> getTransactionItemListForMonth(String accName, String date, YearMonth yearMonth) {
        List<TransactionItem> transactionListForMonth = transactionsRepo.getTransactionListForMonthAccumulatedByDate(accName, yearMonth)
                .stream().collect(Collectors.toList());

        if (transactionListForMonth.isEmpty()) {
            return null;
        }
        //adding previous transaction
        if (transactionListForMonth.get(0).getDate().getDayOfMonth() != 1) {
            TransactionItem previousTransactionForTheMonth = transactionsRepo.getPreviousTransactionForTheMonth(accName, yearMonth);
            if (previousTransactionForTheMonth != null) {
                previousTransactionForTheMonth.setDate(LocalDate.parse(date + "01", DateTimeFormatter.BASIC_ISO_DATE));
                transactionListForMonth.add(0, previousTransactionForTheMonth);
            }
        }
        return transactionListForMonth;
    }

    private String interestToFormattedSting(InterestItem interestItem) {
        return String.format("| %s | %-20s | %7.2f |", interestItem.getDate().format(DateTimeFormatter.BASIC_ISO_DATE), interestItem.getRuleId(),
                interestItem.getRate());
    }
}
