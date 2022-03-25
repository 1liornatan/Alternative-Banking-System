package bank.accounts.impl.exceptions;

public class NegativeAmountException extends Exception {
    private static final String EXCEPTION_MESSAGE = "Negative amount was entered.";

    public NegativeAmountException() {
    }

    @Override
    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }
}
