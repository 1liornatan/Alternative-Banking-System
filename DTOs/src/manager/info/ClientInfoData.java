package manager.info;

import java.util.HashSet;
import java.util.Set;

public class ClientInfoData {
    final Set<String> categories;
    final int balance;

    private ClientInfoData(ClientInfoDataBuilder clientInfoDataBuilder) {
        this.categories = clientInfoDataBuilder.categories;
        this.balance = clientInfoDataBuilder.balance;
    }

    public Set<String> getCategories() {
        return categories;
    }

    public int getBalance() {
        return balance;
    }

    public static class ClientInfoDataBuilder {
        Set<String> categories;
        int balance;

        public ClientInfoDataBuilder() {
            categories = new HashSet<>();
            balance = 0;
        }

        public ClientInfoDataBuilder balance(int balance) {
            this.balance = balance;
            return this;
        }

        public ClientInfoDataBuilder categories(Set<String> categories) {
            this.categories.addAll(categories);
            return this;
        }

        public ClientInfoData build() {
            return new ClientInfoData(this);
        }
    }
}
