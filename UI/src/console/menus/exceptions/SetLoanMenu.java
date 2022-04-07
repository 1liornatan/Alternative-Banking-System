package console.menus.exceptions;

import bank.Bank;
import manager.categories.CategoriesDTO;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class SetLoanMenu {
    int amount;
    Set<String> categories;
    float minInterest;
    int minLoanDuration;
    Bank bankInstance;

    public SetLoanMenu(Bank bank) {
        amount = 0;
        categories = new HashSet<>();
        minInterest = 0;
        minLoanDuration = 0;

        bankInstance = bank;
    }

    public void printMenu() throws NoOptionException {
        System.out.println("Loan Investment requirements: \n1.Set Investment Amount [MUST].\n" +
                "2.Set Loan Category.\n" + "3.Set Loan's Minimum Interest.\n" +
                "4.Set Loan's Minimum Duration.");

        Scanner scanner = new Scanner(System.in);
        switch(scanner.nextInt()) {
            case 1:
                setAmount();
                break;
            case 2:
                setCategory();
                break;
            case 3:
                setInterest();
                break;
            case 4:
                setDuration();
                break;
            default:
                throw new NoOptionException();
        }
    }

    private void setCategory() {
        System.out.println("Choose a category to add:");
        CategoriesDTO categories = bankInstance.getCategories();
    }

    private void setDuration() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type min loan's duration");
        minInterest = scanner.nextInt();
    }

    private void setInterest() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type min amount of interest percentage");
        minInterest = scanner.nextFloat();
    }

    private void setAmount() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type max amount to invest");
        amount = scanner.nextInt();
    }
}
