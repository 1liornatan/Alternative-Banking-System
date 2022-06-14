package bank.logic.manager;

import bank.logic.Bank;
import bank.logic.impl.BankImpl;
import manager.customers.CustomersNames;

public class BankManager {
    private final Bank bankInstance;

    public BankManager() {
        bankInstance = new BankImpl();
    }

    public synchronized void addCustomer(String username) {
        bankInstance.addCustomer(username);
    }

    public synchronized CustomersNames getUsers(){
        return bankInstance.getCustomersNames();
    }

}
