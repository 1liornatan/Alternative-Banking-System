package bank.logic.impl.exceptions;

public class DataAlreadyExistsException extends Exception {
    private final String exceptionMessage;

    public DataAlreadyExistsException(String name) {
        exceptionMessage = "Data with ID '" + name + "' already exists.";
    }

    @Override
    public String getMessage() {
        return exceptionMessage;
    }
}
