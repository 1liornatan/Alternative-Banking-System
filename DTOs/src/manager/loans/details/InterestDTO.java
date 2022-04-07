package manager.loans.details;

public class InterestDTO {
    private final int baseAmount, finalAmount;
    private final float percent;

    public InterestDTO(int baseAmount, int finalAmount, float percent) {
        this.baseAmount = baseAmount;
        this.finalAmount = finalAmount;
        this.percent = percent;
    }

    public int getBaseAmount() {
        return baseAmount;
    }

    public int getFinalAmount() {
        return finalAmount;
    }

    public float getPercent() {
        return percent;
    }
}
