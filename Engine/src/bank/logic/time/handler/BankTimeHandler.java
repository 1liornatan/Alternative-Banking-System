package bank.logic.time.handler;

import bank.logic.time.TimeHandler;

public class BankTimeHandler implements TimeHandler {
    private int currentTime;
    private int previousTime;

    public BankTimeHandler() {
        currentTime = 1;
        previousTime = 1;
    }

    @Override
    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    @Override
    public void setPreviousTime(int previousTime) {
        this.previousTime = previousTime;
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
