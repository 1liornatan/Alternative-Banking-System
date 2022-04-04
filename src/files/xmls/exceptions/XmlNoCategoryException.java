package files.xmls.exceptions;

public class XmlNoCategoryException extends Exception {
    private final static String EXCEPTION_MESSAGE = "Xml is invalid.\nA Category of a loan was not found." ;

    @Override
    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }
}
