package Console.Menus.Exceptions;

public class NoOptionException extends Exception {

    private final String EXCEPTION_MESSAGE = "The option selected does not exist.";

    public NoOptionException() {

    }

    @Override
    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }
}