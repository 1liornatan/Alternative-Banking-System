package manager.customers;

import manager.accounts.AccountDTO;
import manager.loans.LoanDTO;
import manager.loans.LoansDTO;

public class CustomerDTO {
    LoanDTO loan;
    LoansDTO investedLoans;
    LoansDTO requestedLoans;
    AccountDTO account;


    public CustomerDTO(LoanDTO loan, LoansDTO investedLoans, LoansDTO requestedLoans, AccountDTO account) {
        this.loan = loan;
        this.investedLoans = investedLoans;
        this.requestedLoans = requestedLoans;
        this.account = account;
    }

}
