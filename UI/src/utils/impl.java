package utils;

import bank.accounts.Account;
import javafx.util.Pair;

import java.util.Collection;

public class impl {






    public void printCustomersNames() {
        Collection<Pair<Account, Integer>> allPairs = customersAccounts.getAllPairs();
        System.out.println("All customers names:");

        for(Pair<Account,Integer> accountPair : allPairs) {
            System.out.println(accountPair.getKey().getId());
        }
    }
}
