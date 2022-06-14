package bank.logic.impl.exceptions;

public class DataNotFoundException extends Exception {

    private final String exceptionMessage;

    public DataNotFoundException(String name) {
        exceptionMessage = "Data with ID '" + name + "' was not found.";
    }

    @Override
    public String getMessage() {
        return exceptionMessage;
    }
}