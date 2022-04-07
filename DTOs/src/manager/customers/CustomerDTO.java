package manager.customers;

import manager.accounts.AccountDTO;
import manager.loans.LoanDTO;
import manager.loans.LoansDTO;

public class CustomerDTO {
    LoansDTO investedLoans;
    LoansDTO requestedLoans;
    AccountDTO account;


    public CustomerDTO(LoansDTO investedLoans, LoansDTO requestedLoans, AccountDTO account) {
        this.investedLoans = investedLoans;
        this.requestedLoans = requestedLoans;
        this.account = account;
    }

}
