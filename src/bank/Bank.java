package bank;

import bank.accounts.impl.exceptions.NoMoneyException;
import bank.accounts.impl.exceptions.NonPositiveAmountException;
import bank.loans.Loan;

public interface Bank {
    void loadData(String filename);

    void printLoans();

    void advanceOneYaz();

    int getCurrentYaz();

    //    int createLoan(int ownerId, float amount, String category); // returns loan's id
    int withdraw(int accountId, float amount, String description); // returns transaction's id
    int deposit(int accountId, float amount, String description); // returns transaction's id
    int createAccount(String name, float balance); // returns account's id

    void deriskLoan(Loan loan) throws NoMoneyException, NonPositiveAmountException;

    float getDeriskAmount(Loan loan);

    void printCustomers();
}
