package manager.loans.details;

public class YazDTO {
    private final int startedYaz, finishedYaz;

    public int getStartedYaz() {
        return startedYaz;
    }

    public int getFinishedYaz() {
        return finishedYaz;
    }

    public YazDTO(int startedYaz, int finishedYaz) {
        this.startedYaz = startedYaz;
        this.finishedYaz = finishedYaz;
    }
}
