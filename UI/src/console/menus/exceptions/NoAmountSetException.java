package console.menus.exceptions;

public class NoAmountSetException extends Exception {
    private static final String EXCEPTION_MESSAGE = "A positive amount must be set first.";

    @Override
    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }
}
