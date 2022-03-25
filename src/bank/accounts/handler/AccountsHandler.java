package bank.accounts.handler;

import bank.accounts.Account;

import java.util.Set;

public interface AccountsHandler {

    Account getAccountById(int id); // returns null when not exists
    Set<Account> getAccountsByIds(Set<Integer> idSet);
    void addAccount(Account account);
    void addAccounts(Set<Account> accounts);
    boolean isAccountExists(int id);



}
