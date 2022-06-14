package bank.logic;

import bank.logic.accounts.Account;
import bank.logic.accounts.CustomerAccount;
import bank.logic.accounts.impl.exceptions.NoMoneyException;
import bank.logic.accounts.impl.exceptions.NonPositiveAmountException;
import bank.logic.data.storage.DataStorage;
import bank.logic.impl.exceptions.DataNotFoundException;
import bank.logic.loans.Loan;
import bank.logic.loans.interest.exceptions.InvalidPercentException;
import bank.logic.time.TimeHandler;
import bank.logic.transactions.Transaction;
import files.xmls.exceptions.*;
import manager.customers.*;
import manager.investments.*;
import manager.loans.LoanDTO;
import manager.categories.CategoriesDTO;
import manager.loans.LoanData;
import manager.loans.LoansDTO;
import manager.loans.LoansData;
import manager.messages.NotificationsData;
import manager.time.YazSystemDTO;
import manager.transactions.TransactionDTO;
import manager.transactions.TransactionData;
import manager.transactions.TransactionsDTO;
import manager.transactions.TransactionsData;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface Bank {
    void addCustomer(String customerName);

    void listInvestment(InvestmentData data) throws DataNotFoundException;

    void unlistInvestment(InvestmentData data) throws DataNotFoundException;

    void investmentTrade(InvestmentData data) throws DataNotFoundException, NoMoneyException, NonPositiveAmountException;

    InvestmentsSellData getCustomerInvestments(String customerId) throws DataNotFoundException;

    DataStorage<CustomerAccount> getCustomersAccounts();

    DataStorage<Account> getLoanAccounts();

    DataStorage<Transaction> getTransactions();

    DataStorage<Loan> getLoans();

    TimeHandler getTimeHandler();

    void loadData(String filename) throws FileNotFoundException, NotXmlException, XmlNoLoanOwnerException, XmlNoCategoryException, XmlPaymentsException, XmlAccountExistsException, XmlNotFoundException, DataNotFoundException;

    void advanceOneYaz() throws DataNotFoundException, NonPositiveAmountException;

    void advanceOneCycle() throws DataNotFoundException, NonPositiveAmountException;

    void advanceCycle(String loanId) throws DataNotFoundException, NonPositiveAmountException;

    int getCurrentYaz();

    //    int createLoan(int ownerId, float amount, String category); // returns loan's id
    void withdraw(String accountId, int amount, String description) throws NoMoneyException, NonPositiveAmountException, DataNotFoundException; // returns transaction's id
    void deposit(String accountId, int amount, String description) throws NonPositiveAmountException, DataNotFoundException; // returns transaction's id
    void createAccount(String name, int balance); // returns account's id

    void deriskLoan(Loan loan) throws NoMoneyException, NonPositiveAmountException, DataNotFoundException;

    void deriskLoanRequest(String loanId) throws NoMoneyException, NonPositiveAmountException;

    int getDeriskAmount(Loan loan);

    InvestmentsSellData getInvestmentsForSell(String requesterId);

    void setInvestmentsData(InvestmentsData investmentsData) throws DataNotFoundException, NoMoneyException, NonPositiveAmountException;

    void createInvestment(String investor, Loan loan, int amount) throws DataNotFoundException, NoMoneyException, NonPositiveAmountException;

    void setInvestments(String requesterName, List<Loan> loanDataList, int amount) throws DataNotFoundException, NoMoneyException, NonPositiveAmountException;

    LoansData loanAssignmentRequest(RequestDTO requestDTO) throws InvalidPercentException;


    CustomersDTO getCustomersDTO() throws DataNotFoundException;

    CustomerDTO getCustomerDTO(String id) throws DataNotFoundException;

    LoanDTO getLoanDTO(Loan loan);

    List<LoanData> getUnFinishedLoans(String customerId) throws DataNotFoundException;

    Set<String> getCategories();

    CategoriesDTO getCategoriesDTO();

    LoansDTO getAllLoansDTO();

    TransactionDTO getTransactionDTO(Transaction transaction) throws DataNotFoundException;

    TransactionsDTO getTransactionsDTO(Account account) throws DataNotFoundException;

    YazSystemDTO getYazSystemDTO();

    void saveToFile(String filePath) throws IOException;

    void loadFromFile(String filePath) throws IOException, ClassNotFoundException;

    LoanData getLoanData(Loan loan) throws DataNotFoundException;

    CustomerData getCustomerData(CustomerAccount customer) throws DataNotFoundException;

    CustomersData getCustomersData() throws DataNotFoundException;

    LoansData getLoansData() throws DataNotFoundException;

    CustomersNames getCustomersNames();

    LoansData getLoanerData(String customerId);

    LoansData getInvestorData(String customerId);

    TransactionData getTransactionData(Transaction transaction);

    TransactionsData getTransactionsData(String cutsomerId);

    NotificationsData getNotificationsData(String customerId) throws DataNotFoundException;

    void deriskLoanByAmount(String id, int amount);

    void closeLoan(String id) throws DataNotFoundException, NoMoneyException, NonPositiveAmountException;

    PaymentsData getPaymentsData(String customerId) throws DataNotFoundException;

    PaymentsData getAllTransactionsData();

    PaymentsData getAllLoansData();

    PaymentsData getAllCustomersData();
}
