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
import files.xmls.XmlReader;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

public class BankImpl implements Bank {
    private DataStorage<Account> customersAccounts;
    private DataStorage<Account> loanAccounts;
    private DataStorage<Transaction> transactions;
    private DataStorage<Loan> loans;
    private Set<String> categories;

    @Override
    public void loadData(String filename) {
        XmlReader xmlReader = new XmlReader(filename);

        customersAccounts = xmlReader.getCustomersDataStorage();
        loans = xmlReader.getLoansDataStorage();
        categories = xmlReader.getCategoryNames();

        loanAccounts = new BankDataStorage<>();
        transactions = new BankDataStorage<>();
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
