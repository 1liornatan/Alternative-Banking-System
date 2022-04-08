package bank.time.handler;

import bank.time.TimeHandler;

public class BankTimeHandler implements TimeHandler {
    private int currentTime;
    private int previousTime;

    public BankTimeHandler() {
        currentTime = 1;
        previousTime = 1;
    }

    @Override
    public int getCurrentTime() {
        return currentTime;
    }

    @Override
    public int getPreviousTime() {
        return previousTime;
    }

    @Override
    public void advanceTime() {
        previousTime = currentTime;
        currentTime++;
    }
}
