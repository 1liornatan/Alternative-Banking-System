package manager.investments;

import manager.categories.CategoriesDTO;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RequestDTO {
    final int amount;
    final CategoriesDTO categoriesDTO;
    final float minInterest;
    final int minLoanDuration;
    final int maxRelatedLoans;
    final int maxOwnership;
    final String requesterName;

    public RequestDTO(Builder builder) {
        this.amount = builder.amount;
        this.categoriesDTO = builder.categoriesDTO;
        this.minInterest = builder.minInterest;
        this.minLoanDuration = builder.minLoanDuration;
        this.requesterName = builder.requesterName;
        this.maxRelatedLoans = builder.maxRelatedLoans;
        this.maxOwnership = builder.maxOwnership;
    }

    public int getMaxOwnership() {
        return maxOwnership;
    }

    public int getAmount() {
        return amount;
    }

    public CategoriesDTO getCategoriesDTO() {
        return categoriesDTO;
    }

    public float getMinInterest() {
        return minInterest;
    }

    public int getMinLoanDuration() {
        return minLoanDuration;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public int getMaxRelatedLoans() { return maxRelatedLoans;
    }

    public static class Builder {
        final int amount;
        CategoriesDTO categoriesDTO;
        float minInterest;
        int minLoanDuration;
        int maxRelatedLoans;
        int maxOwnership;
        final String requesterName;

        public Builder(String requesterName, int amount) {
            this.amount = amount;
            this.minInterest = 0;
            this.maxRelatedLoans = 0;
            this.minLoanDuration = 0;
            this.maxOwnership = 0;
            categoriesDTO = null;
            this.requesterName = requesterName;
        }

        public Builder maxOwnership(int amount) {
            this.maxOwnership = amount;
            return this;
        }

        public Builder minInterest(float minInterest) {
            this.minInterest = minInterest;
            return this;
        }

        public Builder maxLoans(int maxLoans) {
            this.maxRelatedLoans = maxLoans;
            return this;
        }

        public Builder minDuration(int duration) {
            this.minLoanDuration = duration;
            return this;
        }

        public Builder categories(Set<String> categories) {
            categoriesDTO = new CategoriesDTO(categories);
            return this;
        }

        public RequestDTO build() {
            return new RequestDTO(this);
        }
    }

}
