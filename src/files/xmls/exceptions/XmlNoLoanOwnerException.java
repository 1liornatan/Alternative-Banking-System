package files.xmls.exceptions;

public class XmlNoLoanOwnerException extends Exception {

    private final static String EXCEPTION_MESSAGE = "XML is invalid.\nA loan with no owner in system was found.";


    @Override
    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }
}
