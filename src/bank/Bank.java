package bank;

import bank.accounts.Account;
import bank.accounts.impl.exceptions.NoMoneyException;
import bank.accounts.impl.exceptions.NonPositiveAmountException;
import bank.impl.exceptions.DataNotFoundException;
import bank.loans.Loan;
import files.xmls.exceptions.*;
import javafx.util.Pair;
import manager.customers.CustomerDTO;
import manager.customers.CustomersDTO;
import manager.investments.RequestDTO;
import manager.loans.LoanDTO;
import manager.categories.CategoriesDTO;
import manager.loans.LoansDTO;

import java.io.FileNotFoundException;
import java.util.Collection;

public interface Bank {
    void loadData(String filename) throws FileNotFoundException, NotXmlException, XmlNoLoanOwnerException, XmlNoCategoryException, XmlPaymentsException, XmlAccountExistsException, XmlNotFoundException, DataNotFoundException;

    void printLoans();

    void advanceOneYaz() throws DataNotFoundException, NonPositiveAmountException;

    int getCurrentYaz();

    //    int createLoan(int ownerId, float amount, String category); // returns loan's id
    void withdraw(String accountId, int amount, String description) throws NoMoneyException, NonPositiveAmountException, DataNotFoundException; // returns transaction's id
    void deposit(String accountId, int amount, String description) throws NonPositiveAmountException, DataNotFoundException; // returns transaction's id
    void createAccount(String name, int balance); // returns account's id

    void deriskLoan(Loan loan) throws NoMoneyException, NonPositiveAmountException, DataNotFoundException;

    int getDeriskAmount(Loan loan);

    LoansDTO loanAssignmentRequest(RequestDTO requestDTO);

    void printCustomers() throws DataNotFoundException;

    CustomersDTO getCustomersDTO() throws DataNotFoundException;

    CustomerDTO getCustomerDTO(String id) throws DataNotFoundException;

    LoanDTO getLoanDTO(Loan loan);
    CategoriesDTO getCategories();
}
