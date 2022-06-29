package bank.logic.manager;

import bank.logic.Bank;
import bank.logic.accounts.impl.exceptions.NoMoneyException;
import bank.logic.accounts.impl.exceptions.NonPositiveAmountException;
import bank.logic.impl.BankImpl;
import bank.logic.impl.exceptions.DataNotFoundException;
import bank.logic.loans.interest.exceptions.InvalidPercentException;
import manager.customers.CustomersData;
import manager.customers.CustomersNames;
import manager.info.ClientInfoData;
import manager.investments.*;
import manager.loans.LoansData;
import manager.messages.NotificationsData;
import manager.transactions.TransactionsData;

public class BankManager {
    private final Bank bankInstance;

    public BankManager() {
        bankInstance = new BankImpl();
    }

    public synchronized void addCustomer(String username) {
        bankInstance.addCustomer(username);
    }
    public boolean isUserExists(String username) {
        return bankInstance.isCustomerExists(username);
    }

    public LoansData getRequestedLoans(String username) {
        return bankInstance.getLoanerData(username);
    }
    public LoansData getInvestedLoans(String username) {return bankInstance.getInvestorData(username);}
    public LoansData getIntegrationLoans(RequestDTO requestDTO) throws InvalidPercentException {
        return bankInstance.loanAssignmentRequest(requestDTO);
    }
    public NotificationsData getNotificationsData(String username) throws DataNotFoundException {return bankInstance.getNotificationsData(username);}
    public synchronized CustomersNames getUsers(){
        return bankInstance.getCustomersNames();
    }

    public void setInvestment(InvestmentsData investmentsData) throws Exception {
        bankInstance.setInvestmentsData(investmentsData);
    }

    public void withdraw(String accountId, int amount, String description) throws DataNotFoundException, NoMoneyException, NonPositiveAmountException {
        bankInstance.withdraw(accountId,amount,description);
    }

    public void deposit(String accountId, int amount, String description) throws DataNotFoundException, NonPositiveAmountException {
        bankInstance.deposit(accountId, amount, description);
    }

    public TransactionsData getTransactionsData(String customerId) {
        return bankInstance.getTransactionsData(customerId);
    }

    public PaymentsData getPaymentsData(String username) throws DataNotFoundException {return bankInstance.getPaymentsData(username);}
    public LoansData getUnFinishedLoans(String username) throws DataNotFoundException {
        return bankInstance.getUnfinishedLoans(username);
    }

    public void closeLoan(String id) throws DataNotFoundException, NoMoneyException, NonPositiveAmountException {
        bankInstance.closeLoan(id);
    }
    public void advanceCycle(String loanId) throws DataNotFoundException, NonPositiveAmountException {
        bankInstance.advanceCycle(loanId);
    }

    public void deriskLoanByAmount(String id, int amount) {bankInstance.deriskLoanByAmount(id, amount);}

    public void addLoansFromFile(String username, String filePath) throws Exception {
        bankInstance.addLoansFromFile(username, filePath);
    }

    public ClientInfoData getClientInfo(String customer) throws DataNotFoundException {
        return bankInstance.getClientInfo(customer);
    }

    public CustomersData getCustomersData() throws DataNotFoundException {
        return bankInstance.getCustomersData();
    }

    public LoansData getLoansData() throws DataNotFoundException {return bankInstance.getLoansData();}

    public void investmentTrade(InvestmentData data) throws DataNotFoundException, NoMoneyException, NonPositiveAmountException {
        bankInstance.investmentTrade(data);
    }

    public void listInvestment(InvestmentData data) throws DataNotFoundException {
        bankInstance.listInvestment(data);
    }

    public void unlistInvestment(InvestmentData data) throws DataNotFoundException {
        bankInstance.unlistInvestment(data);
    }

    public InvestmentsSellData getInvestmentsForSell(String requesterId) {
        return bankInstance.getInvestmentsForSell(requesterId);
    }

    public InvestmentsSellData getCustomerInvestments(String customerId) throws DataNotFoundException {
        return getCustomerInvestments(customerId);
    }

    public PaymentsData getAllLoansData() {return bankInstance.getAllLoansData();}
    public PaymentsData getAllCustomersData() {return bankInstance.getAllCustomersData();}
    public PaymentsData getAllTransactionsData() {return bankInstance.getAllTransactionsData();}

    public int getCustomersVersion() {
        return bankInstance.getCustomersVer();
    }

    public int getLoansVersion() {
        return bankInstance.getLoansVer();
    }

    public int getCurrentYaz() {
        return bankInstance.getCurrentYaz();
    }

    public void increaseYaz() throws DataNotFoundException, NonPositiveAmountException {
        bankInstance.advanceOneYaz();
    }
}
