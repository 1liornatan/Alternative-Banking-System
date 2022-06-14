package bank.logic.loans.interest.exceptions;

public class InvalidPercentException extends Exception {
    private static final String EXCEPTION_MESSAGE = "Interest percent must be between 1 to 100.";

    @Override
    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }
}
