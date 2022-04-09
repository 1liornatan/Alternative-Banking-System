package console.menus;

import bank.Bank;
import console.menus.exceptions.NoOptionException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class BonusMenu {

    private final Bank bank;

    public BonusMenu(Bank bank) {
        this.bank = bank;
    }

    public void printMenu() {
        int option = 0;
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("Bonus features menu:\n" +
                    "1.Save current state to file.\n" +
                    "2.Load state from file.\n\n" +
                    "9.Return to main menu.");

            try {
                option = scanner.nextInt();

                switch (option) {
                    case 1:
                        saveData();
                        break;
                    case 2:
                        loadData();
                        break;
                    case 9:
                        break;
                    default:
                        throw new NoOptionException();
                }
            } catch (NoOptionException e) {
                System.out.println(e.getMessage());
            }
        }
        while(option != 9);
    }
    private void saveData() {
        System.out.println("Enter a file name or path");
        Scanner scanner = new Scanner(System.in);

        try {
            String file = scanner.nextLine();
            bank.saveToFile(file);

            System.out.println("Current state saved successfully to: '" + file + "'");
        } catch (InputMismatchException e) {
            System.out.println("Please enter a valid file name.");
        } catch (IOException e) {
            System.out.println("Creating file..");
        }
    }

    private void loadData() {
        System.out.println("Enter a file name or path to load:");
        Scanner scanner = new Scanner(System.in);

        try {
            String file = scanner.nextLine();
            bank.loadFromFile(file);

            System.out.println("System state was loaded successfully.");
        } catch (InputMismatchException e) {
            System.out.println("Please enter a valid file name.");
        } catch (IOException e) {
            System.out.println("File not found.");
        } catch (ClassNotFoundException e) {
            System.out.println("File was corrupted.");
        }
    }
}

