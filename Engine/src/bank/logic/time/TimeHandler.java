package bank.logic.time;

import java.io.Serializable;

public interface TimeHandler extends Serializable {
    void setCurrentTime(int currentTime);

    void setPreviousTime(int previousTime);

    int getCurrentTime();

    int getPreviousTime();

    void advanceTime();
}
