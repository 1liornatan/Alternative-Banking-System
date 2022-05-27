package models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class NotificationModel {
    StringProperty message;
    IntegerProperty yazMade;


    public StringProperty messageProperty() {
        return message;
    }

    public void setMessage(String message) {
        this.message.set(message);
    }


    public IntegerProperty yazMadeProperty() {
        return yazMade;
    }

    public void setYazMade(int yazMade) {
        this.yazMade.set(yazMade);
    }
}
