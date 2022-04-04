package console.menus.exceptions;

import bank.Bank;
import bank.impl.BankImpl;

import java.util.Scanner;

public class MainMenu {
    private Bank bankInstance;


    public MainMenu() {
        bankInstance = new BankImpl();
    }

    public void readXml() {
        System.out.println("Enter Xml full path:");
        Scanner scanner = new Scanner(System.in);
        String fileName = scanner.nextLine();
        bankInstance.loadData(fileName);
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
        bankInstance.withdrawByName(customerName,amount,"Withdraw");

    }

    public void deposit () {
        bankInstance.printCustomersNames();
        System.out.println("Enter customer name:");
        Scanner scanner = new Scanner(System.in);
        String customerName = scanner.nextLine();


        System.out.println("Enter amount to deposit:");
        float amount = scanner.nextFloat();
        bankInstance.depositByName(customerName,amount);

    }

    public void advanceTime() {
        int currYaz = bankInstance.getCurrentYaz();
        System.out.println("Advancing from Yaz " + currYaz + " to Yaz " + (currYaz + 1) + ".");
        bankInstance.advanceOneYaz();
    }
}
