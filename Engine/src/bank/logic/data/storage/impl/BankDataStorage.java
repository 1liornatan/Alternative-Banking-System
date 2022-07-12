package bank.logic.data.storage.impl;

import bank.logic.data.storage.DataStorage;
import bank.logic.data.Singular;
import bank.logic.impl.exceptions.DataNotFoundException;
import bank.logic.impl.exceptions.ReadOnlyException;
import bank.logic.time.TimeHandler;
import javafx.util.Pair;

import java.io.Serializable;
import java.util.*;

public class BankDataStorage<E extends Singular> implements DataStorage<E>, Serializable {
    private final Map<String, DataTimePair> container;
    private final TimeHandler timeHandler;

    public class DataTimePair implements Serializable {
        final E data;
        final int time;

        public DataTimePair(E data) {
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
        DataTimePair dataBox;
        synchronized (this) {
            dataBox = container.get(id);
        }

        if(dataBox == null)
            throw new DataNotFoundException(id);


        int time = dataBox.getTime();

/*        if (timeHandler.isReadOnly() && time > timeHandler.getCurrentTime())
            throw new DataTimeException(id);*/

        return new Pair<>(dataBox.getData(), time);
    }

    @Override
    public E getDataById(String id) throws DataNotFoundException {
        DataTimePair dataBox;
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
        Collection<DataTimePair> values;
        synchronized (this) {
            values = container.values();
        }

        int currentTime = timeHandler.getCurrentTime();

        for (DataTimePair value : values) {
            int time = value.getTime();
            if(time <= currentTime)
                dataCollection.add(new Pair<>(value.getData(), time));
        }
        return dataCollection;
    }

    @Override
    public Collection<E> getDataByIds(Collection<String> ids) throws DataNotFoundException {
        Collection<E> data = new HashSet<>();

        int currentTime = timeHandler.getCurrentTime();

        for(String currId : ids) {
            DataTimePair dataToAdd;
            synchronized (this) {
                dataToAdd = container.get(currId);
            }
            if(dataToAdd == null)
                throw new DataNotFoundException(currId);

            if(dataToAdd.getTime() <= currentTime)
                data.add(dataToAdd.getData());
        }

        return data;
    }

    @Override
    public void addData(E data) {
        if(timeHandler.isReadOnly())
            return;
        DataTimePair pairData = new DataTimePair(data);
        String id = data.getId();

        synchronized (this) {
            container.put(id, pairData);
        }
    }

    @Override
    public void addDataSet(Collection<E> dataSet) {
        if(timeHandler.isReadOnly())
            return;

        synchronized (this) {
            for (E data : dataSet) {
                String id = data.getId();
                DataTimePair f = new DataTimePair(data);
                container.put(id, f);
            }
        }
    }

    @Override
    public String toString() {
        return container.toString();
    }

}
