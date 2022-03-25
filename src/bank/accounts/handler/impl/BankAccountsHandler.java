package bank.accounts.handler.impl;

import bank.accounts.Account;
import bank.accounts.handler.AccountsHandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BankAccountsHandler implements AccountsHandler {
    private Map<Integer, Account> container;

    BankAccountsHandler() {
        container = new HashMap<>();
    }

    @Override
    public boolean isAccountExists(int id) {
        return (container.get(id) != null);
    }

    @Override
    public Account getAccountById(int id) {
        return container.get(id);
    }

    @Override
    public Set<Account> getAccountsByIds(Set<Integer> ids) {
        Set<Account> accounts = new HashSet<>();

        for(Integer currId : ids) {
            accounts.add(container.get(currId));
        }

        return accounts;
    }
}
