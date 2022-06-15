package bank.logic.manager;

import bank.logic.Bank;
import bank.logic.accounts.impl.exceptions.NoMoneyException;
import bank.logic.accounts.impl.exceptions.NonPositiveAmountException;
import bank.logic.impl.BankImpl;
import bank.logic.impl.exceptions.DataNotFoundException;
import bank.logic.loans.interest.exceptions.InvalidPercentException;
import manager.customers.CustomersNames;
import manager.investments.InvestmentsData;
import manager.investments.RequestDTO;
import manager.loans.LoansData;

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

    public LoansData getIntegrationLoans(RequestDTO requestDTO) throws InvalidPercentException {
        return bankInstance.loanAssignmentRequest(requestDTO);
    }

    public synchronized CustomersNames getUsers(){
        return bankInstance.getCustomersNames();
    }

    public void setInvestment(InvestmentsData investmentsData) throws Exception {
        bankInstance.setInvestmentsData(investmentsData);
    }
}
