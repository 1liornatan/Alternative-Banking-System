package screens.customer.loans.integration;

import java.util.HashSet;
import java.util.Set;

public class IntegrationsReqs {

    private final int amount;
    private final int maxOwnership, maxLoans;
    private final int minYaz, minInterest;
    private final Set<String> categories;

    private IntegrationsReqs(Builder builder) {
        this.amount = builder.amount;
        this.maxOwnership = builder.maxOwnership;
        this.maxLoans = builder.maxLoans;
        this.minInterest = builder.minInterest;
        this.minYaz = builder.minYaz;
    }

    public int getAmount() {
        return amount;
    }

    public int getMaxOwnership() {
        return maxOwnership;
    }

    public int getMaxLoans() {
        return maxLoans;
    }

    public int getMinYaz() {
        return minYaz;
    }

    public int getMinInterest() {
        return minInterest;
    }

    public Set<String> getCategories() {
        return categories;
    }

    public static class Builder {
        private final int amount;
        private int maxOwnership, maxLoans;
        private int minYaz, minInterest;
        private Set<String> categories;

        public Builder(int amount) {
            this.amount = amount;
            maxOwnership = 0;
            maxLoans = 0;
            minYaz = 1;
            minInterest = 1;
            categories = new HashSet<>();
        }

        public Builder maxOwnership(int maxOwnership) {
            this.maxOwnership = maxOwnership;
            return this;
        }

        public Builder categories(Set<String> categories) {
            this.categories = categories;
            return this;
        }

        public Builder maxLoans(int maxLoans) {
            this.maxLoans = maxLoans;
            return this;
        }

        public Builder minYaz(int minYaz) {
            this.minYaz = minYaz;
            return this;
        }

        public Builder minInterest(int minInterest) {
            this.minInterest = minInterest;
            return this;
        }

        public IntegrationsReqs build() {
            return new IntegrationsReqs(this);
        }
    }
}
