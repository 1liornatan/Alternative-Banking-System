package manager.customers;

import java.util.List;

public class CustomersDTO {
    final List<CustomerDTO> customers;

    public CustomersDTO(List<CustomerDTO> customers) {
        this.customers = customers;
    }

    public List<CustomerDTO> getCustomers() {
        return customers;
    }

}
