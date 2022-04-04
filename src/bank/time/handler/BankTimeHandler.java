package bank.time.handler;

import bank.time.TimeHandler;

public class BankTimeHandler implements TimeHandler {
    private int currentTime;

    public BankTimeHandler() {
        currentTime = 1;
    }

    @Override
    public int getCurrentTime() {
        return currentTime;
    }

    @Override
    public void advanceTime() {
        currentTime++;
    }
}
