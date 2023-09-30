package com.mlpj.simple.banking.system.cli;

import java.util.Scanner;

public class CLIUtils {
    private CLIUtils() {}

    public static String makeUserPrompt(String prompt) {
        System.out.println(prompt);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public static void displayOutput(String output) {
        System.out.println(output);
    }
}
