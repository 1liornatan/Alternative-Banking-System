package bank.accounts.impl.exceptions;

public class NoMoneyException extends Exception {
    private static final String EXCEPTION_MESSAGE = "Not enough money in the account.";

    public NoMoneyException() {
    }

    @Override
    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }
}
