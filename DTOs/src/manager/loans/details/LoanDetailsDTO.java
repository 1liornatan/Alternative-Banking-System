package manager.loans.details;

public class LoanDetailsDTO {
    private final String name, category, status;

    public LoanDetailsDTO(String name, String category, String status) {
        this.name = name;
        this.category = category;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getStatus() {
        return status;
    }
}
