package bank.impl;

import bank.Bank;
import bank.accounts.Account;
import bank.accounts.impl.CustomerAccount;
import bank.accounts.impl.exceptions.NoMoneyException;
import bank.accounts.impl.exceptions.NonPositiveAmountException;
import bank.data.storage.DataStorage;
import bank.data.storage.impl.BankDataStorage;
import bank.loans.Loan;
import bank.transactions.Transaction;
import files.schema.generated.AbsCategories;
import files.schema.generated.AbsCustomer;
import files.schema.generated.AbsDescriptor;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class BankImpl implements Bank {
    private BankDataStorage<Account> customersAccounts;
    private BankDataStorage<Account> loanAccounts;
    private BankDataStorage<Transaction> transactions;
    private BankDataStorage<Loan> loans;

    public BankImpl() {
        customersAccounts = new BankDataStorage<>();
        loanAccounts = new BankDataStorage<>();
        transactions = new BankDataStorage<>();
        loans = new BankDataStorage<>();
    }
    @Override
    public void loadData(String filename) {

    }
    @Override
    public int withdraw(int accountId, float amount, String description) {
        Account account = customersAccounts.getDataById(accountId);

        try {
            Transaction transaction = account.withdraw(amount, description);
            transactions.addData(transaction);

            return transaction.getId();
        } catch (NonPositiveAmountException e) {
            e.printStackTrace();
        } catch (NoMoneyException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public int deposit(int accountId, float amount, String description) {
        Account account = customersAccounts.getDataById(accountId);

        try {
            Transaction transaction = account.deposit(amount, description);
            transactions.addData(transaction);

            return transaction.getId();
        } catch (NonPositiveAmountException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public int createAccount(String name, float balance) {
        Account account = new CustomerAccount(name, balance);

        customersAccounts.addData(account);

        return account.getId();
    }
}
