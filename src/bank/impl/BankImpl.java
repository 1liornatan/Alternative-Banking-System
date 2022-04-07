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
import bank.loans.LoanStatus;
import bank.loans.handler.impl.BankLoanHandler;
import bank.time.TimeHandler;
import bank.time.handler.BankTimeHandler;
import bank.transactions.Transaction;
import files.xmls.XmlReader;
import files.xmls.exceptions.*;
import javafx.util.Pair;

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
    public void loadData(String filename) throws NotXmlException, XmlNoLoanOwnerException, XmlNoCategoryException, XmlPaymentsException, XmlAccountExistsException, XmlNotFoundException, DataNotFoundException {
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
    public void advanceOneYaz() throws DataNotFoundException, NonPositiveAmountException {
        timeHandler.advanceTime();
        loanHandler.oneCycle();
    }

    @Override
    public int getCurrentYaz() {
        return timeHandler.getCurrentTime();
    }

    @Override
    public void withdraw(String accountId, int amount, String description) throws NoMoneyException, NonPositiveAmountException, DataNotFoundException {
        Account account = customersAccounts.getDataById(accountId);
        Transaction transaction = account.withdraw(amount, description);
        transactions.addData(transaction);
    }

    @Override
    public void deposit(String accountId, int amount, String description) throws NonPositiveAmountException, DataNotFoundException {
        Account account = customersAccounts.getDataById(accountId);
        Transaction transaction = account.deposit(amount, description);
        transactions.addData(transaction);
    }

    @Override
    public void createAccount(String name, int balance) {
        Account account = new CustomerAccount(name, balance);

        customersAccounts.addData(account);
    }

    @Override
    public void deriskLoan(Loan loan) throws NoMoneyException, NonPositiveAmountException, DataNotFoundException {
        loanHandler.deriskLoan(loan);
    }

    @Override
    public int getDeriskAmount(Loan loan) {
        return loan.getDeriskAmount();
    }


    @Override
    public void printCustomers() throws DataNotFoundException {
        Collection<Pair<Account, Integer>> allCustomers = customersAccounts.getAllPairs();

        for(Pair<Account, Integer> pairOfAccount : allCustomers) {
            Account currAccount = pairOfAccount.getKey();
            System.out.println(currAccount.toString());
            System.out.println("All account's transactions:");
            for(Transaction transaction : currAccount.getTransactions()) {
                Pair<Transaction, Integer> currTransaction = transactions.getDataPair(transaction.getId());
                System.out.println("(" + currTransaction.getKey().toString() +
                        ", Yaz made at: " + currTransaction.getValue() + ")");
            }

            System.out.println("All account's requested loans:");
            for(Loan loan : currAccount.getLoansRequested()) {
                printLoan(loan);
            }

            System.out.println("All account's invested loans:");
            for(Loan loan : currAccount.getLoansInvested()) {
                printLoan(loan);
            }
        }
    }

    private void printLoan(Loan loan) throws DataNotFoundException {
        LoanStatus loanStatus = loan.getStatus();

        System.out.println("Loan name: " + loan.getId() + ", Category: " + loan.getCategory() + ", Base amount: " +
                loan.getBaseAmount() + ", Total amount: " + loan.getFinalAmount() + ", Pay Every: " +
                loan.getCyclesPerPayment() + " Yaz, Interest percent: " +
                loan.getInterestPercent() + "%, Status: " + loanStatus.toString());

        switch(loanStatus) {
            case PENDING:
                System.out.println("[Money left for becoming Active: " +
                        (loan.getAmountToActive()) + "]");
                break;

            case ACTIVE:
                int cyclesPerPayment = loan.getCyclesPerPayment();
                int nextYaz = loan.getNextYaz();
                System.out.println("[Next payment is in: " + nextYaz +
                        "Yaz, Payment amount: " + loan.getPayment() + ".]");
                break;

            case FINISHED:
                System.out.println("[Yaz Started: " + loan.getStartedYaz() + ", Yaz Finished: " + loan.getFinishedYaz() + ".]");
                break;

            case RISK:
                int missingCycles = loan.getMissingCycles();
                System.out.println("[Missing payments: " + missingCycles + ", Missing Amount: " + getDeriskAmount(loan) + ".]");
                break;
        }

    }

    @Override
    public void getCustomersNames() {
        Collection<Pair<Account, Integer>> allPairs = customersAccounts.getAllPairs();
        System.out.println("All customers names:");

        for(Pair<Account,Integer> accountPair : allPairs) {
            System.out.println(accountPair.getKey().getId());
        }
    }

}
