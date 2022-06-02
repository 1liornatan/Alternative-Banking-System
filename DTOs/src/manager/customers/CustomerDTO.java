package manager.customers;

import manager.accounts.AccountDTO;
import manager.loans.LoanDTO;
import manager.loans.LoansDTO;

public class CustomerDTO {
    final LoansDTO investedLoans;
    final LoansDTO requestedLoans;
    final AccountDTO account;
    final String name;

    public LoansDTO getInvestedLoans() {
        return investedLoans;
    }

    public LoansDTO getRequestedLoans() {
        return requestedLoans;
    }

    public AccountDTO getAccount() {
        return account;
    }

    public CustomerDTO(LoansDTO investedLoans, LoansDTO requestedLoans, AccountDTO account) {
        this.investedLoans = investedLoans;
        this.requestedLoans = requestedLoans;
        this.account = account;
        this.name = account.getName();
    }

    public String getName() {
        return name;
    }
}
