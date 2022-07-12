package bank.logic.time;

import java.io.Serializable;

public interface TimeHandler extends Serializable {

    void setReadOnly(boolean mode);

    boolean isReadOnly();

    void setCurrentTime(int currentTime);

    void setPreviousTime(int previousTime);

    int getCurrentTime();

    int getPreviousTime();

    void advanceTime();

    void rewindTime(int time);

    void resetRewind();
}
