package bank.time;

public interface TimeHandler {
    int getCurrentTime();

    int getPreviousTime();

    void advanceTime();
}
