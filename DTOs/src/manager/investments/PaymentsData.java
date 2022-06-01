package manager.investments;

import java.util.List;

public class PaymentsData {
    public static class PaymentsDataBuilder {
        List<Integer> payments;
        List<Integer> amount;

        public PaymentsDataBuilder payments(List<Integer> payments) {
            this.payments = payments;
            return this;
        }

        public PaymentsDataBuilder amount(List<Integer> amount) {
            this.amount = amount;
            return this;
        }

        public PaymentsData build() {
            return new PaymentsData(this);
        }
    }

    final List<Integer> payments;
    final List<Integer> amount;

    private PaymentsData(PaymentsDataBuilder builder) {
        this.payments = builder.payments;
        this.amount = builder.amount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public List<Integer> getAmount() {
        return amount;
    }
}
