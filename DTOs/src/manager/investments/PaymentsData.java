package manager.investments;

import manager.info.ClientInfoData;

import java.util.ArrayList;
import java.util.List;

public class PaymentsData {
    private final int forecastVer;
    private final List<Integer> payments;
    private final List<Integer> amount;


    public static class PaymentsDataBuilder {
        List<Integer> payments;
        List<Integer> amount;
        private int forecastVer;

        public PaymentsDataBuilder() {
            payments = new ArrayList<>();
            amount = new ArrayList<>();
            forecastVer = 0;
        }
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

        public PaymentsDataBuilder version(int forecastVer) {
            this.forecastVer = forecastVer;
            return this;
        }
    }

    private PaymentsData(PaymentsDataBuilder builder) {
        this.payments = builder.payments;
        this.amount = builder.amount;
        this.forecastVer = builder.forecastVer;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public List<Integer> getAmount() {
        return amount;
    }

    public int getForecastVer() {
        return forecastVer;
    }
}
