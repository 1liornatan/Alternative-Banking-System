package bank.impl;

import bank.Bank;
import bank.accounts.Account;
import bank.accounts.impl.CustomerAccount;
import bank.accounts.impl.exceptions.NoMoneyException;
import bank.accounts.impl.exceptions.NonPositiveAmountException;
import bank.data.storage.DataStorage;
import bank.data.storage.impl.BankDataStorage;
import bank.impl.exceptions.DataNotFoundException;
import bank.loans.Loan;
import bank.loans.handler.impl.BankLoanHandler;
import bank.time.TimeHandler;
import bank.time.handler.BankTimeHandler;
import bank.transactions.Transaction;
import files.xmls.XmlReader;
import files.xmls.exceptions.NotXmlException;
import javafx.util.Pair;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Set;

public class BankImpl implements Bank {
    private DataStorage<Account> customersAccounts;
    private DataStorage<Account> loanAccounts;
    private DataStorage<Transaction> transactions;
    private DataStorage<Loan> loans;
    private Set<String> categories;
    private BankLoanHandler loanHandler;
    private TimeHandler timeHandler;



    @Override
    public void loadData(String filename) throws FileNotFoundException, NotXmlException {
        TimeHandler timeHandler = new BankTimeHandler();
        XmlReader xmlReader = new XmlReader(filename, timeHandler);

        if(xmlReader.isValid()) {

            customersAccounts = xmlReader.getCustomersDataStorage();
            loans = xmlReader.getLoansDataStorage();
            categories = xmlReader.getCategoryNames();

            loanAccounts = new BankDataStorage<>(timeHandler);
            transactions = new BankDataStorage<>(timeHandler);

            loanHandler = new BankLoanHandler(transactions, loans, customersAccounts);

            this.timeHandler = timeHandler;
        }
    }

    @Override
    public void printLoans() {
        loanHandler.printAllLoans();
    }

    @Override
    public void advanceOneYaz() throws DataNotFoundException {
        timeHandler.advanceTime();
        loanHandler.oneCycle();
    }

    @Override
    public int getCurrentYaz() {
        return timeHandler.getCurrentTime();
    }

    @Override
    public void withdraw(String accountId, float amount, String description) throws NoMoneyException, NonPositiveAmountException, DataNotFoundException {
        Account account = customersAccounts.getDataById(accountId);
        Transaction transaction = account.withdraw(amount, description);
        transactions.addData(transaction);
    }

    @Override
    public void deposit(String accountId, float amount, String description) throws NonPositiveAmountException, DataNotFoundException {
        Account account = customersAccounts.getDataById(accountId);
        Transaction transaction = account.deposit(amount, description);
        transactions.addData(transaction);
    }

    @Override
    public void createAccount(String name, float balance) {
        Account account = new CustomerAccount(name, balance);

        customersAccounts.addData(account);
    }

    @Override
    public void deriskLoan(Loan loan) throws NoMoneyException, NonPositiveAmountException, DataNotFoundException {
        loanHandler.deriskLoan(loan);
    }

    @Override
    public float getDeriskAmount(Loan loan) {
        return loanHandler.getDeriskAmount(loan);
    }

    @Override
    public void printCustomers() {
        System.out.println(customersAccounts.toString());
    }

    @Override
    public void printCustomersNames() {
        Collection<Pair<Account, Integer>> allPairs = customersAccounts.getAllPairs();
        System.out.println("All customers names:");

        for(Pair<Account,Integer> accountPair : allPairs) {
            System.out.println(accountPair.getKey().getId());
        }
    }

}
