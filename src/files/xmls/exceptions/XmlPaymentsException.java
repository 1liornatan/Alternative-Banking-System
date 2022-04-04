package files.xmls.exceptions;

public class XmlPaymentsException extends Exception {

    private final static String EXCEPTION_MESSAGE = "Xml is invalid.\nA Loan with invalid payments per Yaz was found." ;

    @Override
    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }

}
