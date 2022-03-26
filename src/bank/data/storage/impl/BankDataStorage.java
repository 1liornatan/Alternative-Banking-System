package bank.data.storage.impl;

import bank.data.storage.DataStorage;
import bank.data.Singular;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BankDataStorage<E extends Singular> implements DataStorage<E> {
    private Map<Integer, E> container;

    public BankDataStorage() {
        container = new HashMap<>();
    }

    @Override
    public boolean isDataExists(int id) {
        return (container.get(id) != null);
    }

    @Override
    public E getDataById(int id) {
        return container.get(id);
    }

    @Override
    public Set<E> getDataByIds(Set<Integer> ids) {
        Set<E> data = new HashSet<>();

        for(Integer currId : ids) {
            data.add(container.get(currId));
        }

        return data;
    }

    @Override
    public void addData(E account) {
        container.put(account.getId(), account);
    }

    @Override
    public void addDataSet(Set<E> dataSet) {
        for(E currAcc : dataSet) {
            container.put(currAcc.getId(), currAcc);
        }
    }

    @Override
    public String toString() {
        return "BankDataStorage{" +
                "container=" + container +
                '}';
    }
}
