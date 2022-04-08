package manager.time;

public class YazSystemDTO {
    private int currentYaz;
    private int previousYaz;

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
