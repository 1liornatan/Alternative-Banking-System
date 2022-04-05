package bank;

import bank.accounts.impl.exceptions.NoMoneyException;
import bank.accounts.impl.exceptions.NonPositiveAmountException;
import bank.impl.exceptions.DataNotFoundException;
import bank.loans.Loan;
import files.xmls.exceptions.*;

import java.io.FileNotFoundException;

public interface Bank {
    void loadData(String filename) throws FileNotFoundException, NotXmlException, XmlNoLoanOwnerException, XmlNoCategoryException, XmlPaymentsException, XmlAccountExistsException;

    void printLoans();

    void advanceOneYaz() throws DataNotFoundException;

    int getCurrentYaz();

    //    int createLoan(int ownerId, float amount, String category); // returns loan's id
    void withdraw(String accountId, float amount, String description) throws NoMoneyException, NonPositiveAmountException, DataNotFoundException; // returns transaction's id
    void deposit(String accountId, float amount, String description) throws NonPositiveAmountException, DataNotFoundException; // returns transaction's id
    void createAccount(String name, float balance); // returns account's id

    void deriskLoan(Loan loan) throws NoMoneyException, NonPositiveAmountException, DataNotFoundException;

    float getDeriskAmount(Loan loan);

    void printCustomers();

    void printCustomersNames();

}
