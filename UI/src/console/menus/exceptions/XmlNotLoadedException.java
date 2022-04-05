package console.menus.exceptions;

public class XmlNotLoadedException extends Exception {
    private static final String EXCEPTION_MESSAGE = "A valid XML should be loaded before using this option.";

    @Override
    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }
}
