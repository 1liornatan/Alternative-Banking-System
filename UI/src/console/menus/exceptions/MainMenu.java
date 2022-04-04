package console.menus.exceptions;

import bank.Bank;
import bank.accounts.impl.exceptions.NoMoneyException;
import bank.accounts.impl.exceptions.NonPositiveAmountException;
import bank.impl.BankImpl;
import bank.impl.exceptions.DataNotFoundException;
import files.xmls.exceptions.NotXmlException;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class MainMenu {
    private final Bank bankInstance;


    public MainMenu() {
        bankInstance = new BankImpl();
    }
    public static void main(String[] args) {
        MainMenu a = new MainMenu();

        a.readXml();

        a.printCustomers();

        a.deposit();

        a.printCustomers();
    }
    public void readXml() {
        System.out.println("Enter Xml full path:");
        Scanner scanner = new Scanner(System.in);
        String fileName = scanner.nextLine();
        try {
            bankInstance.loadData(fileName);
        } catch (FileNotFoundException | NotXmlException e) {
            System.out.println(e.getMessage());
        } finally {
            printMenu();
        }
    }

    public void printLoans() {
        bankInstance.printLoans();
    }

    public void printCustomers() {
        bankInstance.printCustomers();
    }

    public void withdraw () {
        bankInstance.printCustomersNames();
        System.out.println("Enter customer name:");
        Scanner scanner = new Scanner(System.in);
        String customerName = scanner.nextLine();


        System.out.println("Enter amount to withdraw:");
        float amount = scanner.nextFloat();
        try {
            bankInstance.withdraw(customerName,amount,"Basic Withdraw");
        } catch (NoMoneyException | NonPositiveAmountException | DataNotFoundException e) {
            System.out.println(e.getMessage());
        } finally {
            this.printMenu();
        }

    }

    public void deposit () {
        bankInstance.printCustomersNames();
        System.out.println("Enter customer name:");
        Scanner scanner = new Scanner(System.in);
        String customerName = scanner.nextLine();


        System.out.println("Enter amount to deposit:");
        float amount = scanner.nextFloat();
        try {
            bankInstance.deposit(customerName, amount, "Basic Deposit");
        } catch (DataNotFoundException | NonPositiveAmountException e) {
            System.out.println(e.getMessage());
        }
        finally {
            this.printMenu();
        }

    }

    public void printMenu() {
    }

    public void advanceTime() {
        int currYaz = bankInstance.getCurrentYaz();
        System.out.println("Advancing from Yaz " + currYaz + " to Yaz " + (currYaz + 1) + ".");
        try {
            bankInstance.advanceOneYaz();
        } catch (DataNotFoundException e) {
            System.out.println(e.getMessage());
        } finally {
            printMenu();
        }
    }
}
