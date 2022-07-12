package bank.logic.impl.exceptions;

public class ReadOnlyException extends Exception {

    private final String exceptionMessage;

    public ReadOnlyException(String name) {
        exceptionMessage = "Cannot add Data with ID '" + name + "'. The system is in rewind mode.";
    }

    @Override
    public String getMessage() {
        return exceptionMessage;
    }
}