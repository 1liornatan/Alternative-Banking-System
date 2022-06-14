package files.xmls.exceptions;

public class NotXmlException extends Exception {

    private final String exceptionMessage;


    public NotXmlException(String fileName) {
        this.exceptionMessage = "The file '" + fileName + "' is not a type of XML.";
    }

    @Override
    public String getMessage() {
        return exceptionMessage;
    }
}
