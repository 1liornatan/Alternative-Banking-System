package manager.accounts;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class AccountsDTO {
    List<AccountDTO>  accountsDTO;

    public AccountsDTO() {
        accountsDTO = new ArrayList<>();
    }

    public void addAccount(AccountDTO accountDTO) {
        accountsDTO.add(accountDTO);
    }

    public List<AccountDTO> getList() {
        return accountsDTO;
    }

}
