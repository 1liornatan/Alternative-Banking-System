package files.xmls.exceptions;

public class XmlNotFoundException extends Exception {
    private final String exceptionMessage;


    public XmlNotFoundException(String fileName) {
        this.exceptionMessage = "The file '" + fileName + "' was not found.";
    }

    @Override
    public String getMessage() {
        return exceptionMessage;
    }
}
