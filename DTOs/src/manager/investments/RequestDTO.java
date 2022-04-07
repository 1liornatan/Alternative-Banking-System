package manager.investments;

import manager.categories.CategoriesDTO;

import java.util.Set;

public class RequestDTO {
    int amount;
    CategoriesDTO categoriesDTO;
    float minInterest;
    int minLoanDuration;
    String requesterName;

    public RequestDTO(String requesterName, int amount, CategoriesDTO categories, float minInterest, int minLoanDuration) {
        this.amount = amount;
        this.categoriesDTO = categories;
        this.minInterest = minInterest;
        this.minLoanDuration = minLoanDuration;
        this.requesterName = requesterName;
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
}
