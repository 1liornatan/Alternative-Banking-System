package manager.accounts;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class AccountsDTO {
    final List<AccountDTO>  accountsDTO;

    public AccountsDTO(List<AccountDTO> accountsDTO) {
        this.accountsDTO = accountsDTO;
    }

    public List<AccountDTO> getList() {
        return accountsDTO;
    }

}
