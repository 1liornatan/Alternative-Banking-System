package bank.data.storage.impl;

import bank.data.storage.DataStorage;
import bank.data.Singular;

import java.util.*;

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
    public Collection<E> getAll() {
        return container.values();
    }
    @Override
    public Collection<E> getDataByIds(Collection<Integer> ids) {
        Collection<E> data = new HashSet<>();

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
    public void addDataSet(Collection<E> dataSet) {
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
