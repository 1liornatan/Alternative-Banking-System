package manager.transactions;

import java.util.List;

public class TransactionsDTO {
    final List<TransactionDTO> transactions;

    public TransactionsDTO(List<TransactionDTO> transactions) {
        this.transactions = transactions;
    }

    public List<TransactionDTO> getTransactions() {
        return transactions;
    }
}
