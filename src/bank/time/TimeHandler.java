package bank.time;

import java.io.Serializable;

public interface TimeHandler extends Serializable {
    int getCurrentTime();

    int getPreviousTime();

    void advanceTime();
}
