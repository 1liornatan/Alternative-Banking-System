package files.xmls.exceptions;

public class XmlAccountExistsException extends Exception {
    private final static String EXCEPTION_MESSAGE = "Xml is invalid.\nA name with more than one account was found." ;

    @Override
    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }
}
