package bank.logic.time.handler;

import bank.logic.time.TimeHandler;

public class BankTimeHandler implements TimeHandler {
    private int currentTime;
    private int previousTime;
    private boolean readOnly;

    public BankTimeHandler() {
        currentTime = 1;
        previousTime = 1;
        readOnly = false;
    }

    @Override
    public void setReadOnly(boolean mode) {
        readOnly = mode;
    }

    @Override
    public boolean isReadOnly() {
        return readOnly;
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

    @Override
    public void rewindTime(int time) {
        previousTime = currentTime;
        currentTime = time;
        setReadOnly(true);
    }

    @Override
    public void resetRewind() {
        currentTime = previousTime;
        previousTime = currentTime - 1;
        setReadOnly(false);
    }

}
