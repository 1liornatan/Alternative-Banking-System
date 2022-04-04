package bank.data.storage.impl;

import bank.data.storage.DataStorage;
import bank.data.Singular;
import bank.time.TimeHandler;
import javafx.util.Pair;

import java.util.*;

public class BankDataStorage<E extends Singular> implements DataStorage<E> {
    private Map<Integer, F> container;
    private TimeHandler timeHandler;

    public class F {
        E data;
        int time;

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
    public boolean isDataExists(int id) {
        return (container.get(id) != null);
    }

    @Override
    public Pair<E, Integer> getDataPair(int id) {
        F dataBox = container.get(id);
        return new Pair<E, Integer>(dataBox.getData(), dataBox.getTime());
    }

    @Override
    public E getDataById(int id) {
        F dataBox = container.get(id);
        return dataBox.getData();
    }

    @Override
    public Collection<Pair<E, Integer>> getAllPairs() {
        Collection<Pair<E, Integer>> dataCollection = new ArrayList<>();
        Collection<F> values = container.values();

        for(F value : values) {
            dataCollection.add(new Pair<>(value.getData(), value.getTime()));
        }

        return dataCollection;
    }

    @Override
    public Collection<E> getDataByIds(Collection<Integer> ids) {
        Collection<E> data = new HashSet<>();

        for(Integer currId : ids) {
            data.add(container.get(currId).getData());
        }

        return data;
    }

    @Override
    public void addData(E data) {
        container.put(data.getId(), new F(data));
    }

    @Override
    public void addDataSet(Collection<E> dataSet) {
        for(E data : dataSet) {
            container.put(data.getId(), new F(data));
        }
    }

    @Override
    public String toString() {
        return container.toString();
    }
}
