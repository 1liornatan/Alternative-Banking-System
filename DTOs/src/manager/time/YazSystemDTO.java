package manager.time;

public class YazSystemDTO {
    private final int currentYaz;
    private final int previousYaz;

    public YazSystemDTO(int currentYaz, int previousYaz) {
        this.currentYaz = currentYaz;
        this.previousYaz = previousYaz;
    }

    public int getCurrentYaz() {
        return currentYaz;
    }

    public int getPreviousYaz() {
        return previousYaz;
    }
}
