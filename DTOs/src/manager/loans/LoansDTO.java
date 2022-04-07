package manager.loans;

import java.util.List;

public class LoansDTO {
    List<LoanDTO> loansList;

    public List<LoanDTO> getLoansList() {
        return loansList;
    }

    public LoansDTO(List<LoanDTO> loansList) {
        this.loansList = loansList;
    }
}
