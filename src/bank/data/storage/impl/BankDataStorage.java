package bank.data.storage.impl;

import bank.data.storage.DataStorage;
import bank.data.Singular;
import bank.impl.exceptions.DataNotFoundException;
import bank.time.TimeHandler;
import javafx.util.Pair;

import java.io.Serializable;
import java.util.*;

public class BankDataStorage<E extends Singular> implements DataStorage<E>, Serializable {
    private final Map<String, F> container;
    private final TimeHandler timeHandler;

    public class F implements Serializable {
        final E data;
        final int time;

        public F(E data) {
            this.data = data;
            this.time = timeHandler.getCurrentTime();
        }

        public E getData() {
            return data;
        }

        public int getTime() {
            return time;
        }

        @Override
        public String toString() {
            return "    " + data +
                    "\nTime: " + time;
        }
    }

    public BankDataStorage(TimeHandler timeHandler) {
        container = new HashMap<>();
        this.timeHandler = timeHandler;
    }

    @Override
    public boolean isDataExists(String id) {
        return (container.get(id) != null);
    }

    @Override
    public void remove(String id) {
        container.remove(id);
    }
    @Override
    public Pair<E, Integer> getDataPair(String id) throws DataNotFoundException {
        F dataBox;
        synchronized (this) {
            dataBox = container.get(id);
        }

        if(dataBox == null)
            throw new DataNotFoundException(id);

        return new Pair<>(dataBox.getData(), dataBox.getTime());
    }

    @Override
    public E getDataById(String id) throws DataNotFoundException {
        F dataBox;
        synchronized (this) {
            dataBox = container.get(id);
        }
        if(dataBox == null)
            throw new DataNotFoundException(id);

        return dataBox.getData();
    }

    @Override
    public Collection<Pair<E, Integer>> getAllPairs() {
        Collection<Pair<E, Integer>> dataCollection = new ArrayList<>();
        Collection<F> values;
        synchronized (this) {
            values = container.values();
        }

        for (F value : values) {
            dataCollection.add(new Pair<>(value.getData(), value.getTime()));
        }
        return dataCollection;
    }

    @Override
    public Collection<E> getDataByIds(Collection<String> ids) throws DataNotFoundException {
        Collection<E> data = new HashSet<>();

        for(String currId : ids) {
            F dataToAdd;
            synchronized (this) {
                dataToAdd = container.get(currId);
            }
            if(dataToAdd == null)
                throw new DataNotFoundException(currId);

            data.add(dataToAdd.getData());
        }

        return data;
    }

    @Override
    public void addData(E data) {
        F pairData = new F(data);
        String id = data.getId();

        synchronized (this) {
            container.put(id, pairData);
        }
    }

    @Override
    public void addDataSet(Collection<E> dataSet) {
        synchronized (this) {
            for (E data : dataSet) {
                String id = data.getId();
                F f = new F(data);
                container.put(id, f);
            }
        }
    }

    @Override
    public String toString() {
        return container.toString();
    }

}
