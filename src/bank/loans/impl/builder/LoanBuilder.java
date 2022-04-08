package bank.loans.impl.builder;

import java.io.Serializable;
import java.util.Objects;

public class LoanBuilder implements Serializable {
    private final String ownerId;
    private final String category, idName;

    public LoanBuilder(String ownerId, String category, String idName) {
        this.ownerId = ownerId;
        this.category = category;
        this.idName = idName;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getCategory() {
        return category;
    }

    public String getIdName() {
        return idName;
    }

    @Override
    public String toString() {
        return "LoanBuilder{" +
                "ownerId=" + ownerId +
                ", category='" + category + '\'' +
                ", idName='" + idName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoanBuilder that = (LoanBuilder) o;
        return Objects.equals(idName, that.idName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idName);
    }
}
