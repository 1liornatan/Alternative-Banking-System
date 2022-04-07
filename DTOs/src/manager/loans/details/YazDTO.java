package manager.loans.details;

public class YazDTO {
    private final int startedYaz, finishedYaz, nextPaymentYaz;

    public int getStartedYaz() {
        return startedYaz;
    }

    public int getFinishedYaz() {
        return finishedYaz;
    }

    public int getNextPaymentYaz() {
        return nextPaymentYaz;
    }

    public YazDTO(int startedYaz, int finishedYaz, int nextPaymentYaz) {
        this.startedYaz = startedYaz;
        this.finishedYaz = finishedYaz;
        this.nextPaymentYaz = nextPaymentYaz;
    }
}
