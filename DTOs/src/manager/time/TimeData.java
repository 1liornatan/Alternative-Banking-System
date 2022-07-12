package manager.time;

public class TimeData {
    private int time;
    private boolean readOnly;

    public TimeData(int time, boolean readOnly) {
        this.time = time;
        this.readOnly = readOnly;
    }

    public int getTime() {
        return time;
    }

    public boolean isReadOnly() {
        return readOnly;
    }
}
