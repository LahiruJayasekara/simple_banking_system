package com.mlpj.simple.banking.system.util;

public class Constants {
    private Constants() {
    }

    public static final String INITIAL_PROMPT = """
            Welcome to AwesomeGIC Bank! What would you like to do?
            [T] Input transactions
            [I] Define interest rules
            [P] Print statement
            [Q] Quit""";
    public static final String INITIAL_PROMPT_VALIDATION_REGEX = "^[tTiIpPqQ]$";


    public static final String INPUT_TRANSACTIONS_PROMPT = """
            Please enter transaction details in <Date> <Account> <Type> <Amount> format\s
            (or enter blank to go back to main menu):""";
    /*
    Constraints
    * Account name should be within 2-20 characters
    * amount should be less than or equal to a 10-digit number
     */
    public static final String INPUT_TRANSACTIONS_PROMPT_VALIDATION_REGEX = "^\\d{8} .{2,20} [DW] [1-9]\\d{0,9}\\.\\d{2}$";


    public static final String INPUT_INTEREST_RULE_PROMPT = """
            Please enter interest rules details in <Date> <RuleId> <Rate in %> format\s
            (or enter blank to go back to main menu):""";
    /*
    Constraints
    * Interest rule name should be within 2-20 characters
    * interest can have only 2 decimal places
     */
    public static final String INPUT_INTEREST_RULE_VALIDATION_REGEX = "^\\d{8} .{2,20} \\d\\d?\\.\\d{2}$";


    public static final String RETRIEVE_PRINT_STATEMENT_PROMPT = """
            Please enter account and month to generate the statement <Account> <Year><Month>
            (or enter blank to go back to main menu):""";
    /*
    Constraints
    * Account name should be within 2-20 characters
     */
    public static final String RETRIEVE_PRINT_STATEMENT_PROMPT_VALIDATION_REGEX = "^.{2,20} \\d{6}$";

    public static final String THANK_YOU_MESSAGE = """
            Thank you for banking with AwesomeGIC Bank.
            Have a nice day!
            """;

    public static final String STATEMENT_HEADERS_WITHOUT_BALANCE = "| Date     | Txn Id      | Type |    Amount   |";
    public static final String INTEREST_STATEMENT_HEADERS = "| Date     | RuleId               | Rate(%) |";

    public static final String INVALID_INPUT = "Invalid input!, please check again";
    public static final String INSUFFICIENT_BALANCE = "Insufficient balance in your account";
    public static final String OLDER_TRANSACTION_DATE = "Transaction date is older than the last transaction";

}
