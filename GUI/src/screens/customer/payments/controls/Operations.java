package screens.customer.payments.controls;

public enum Operations {
    CLOSE("payment_close"),
    DEBT("payment_debt"),
    CYCLE("payment_active");

    private final String requestProp;

    Operations(String requestProp) {
        this.requestProp = requestProp;
    }

    @Override
    public String toString() {
        return requestProp;
    }
}
