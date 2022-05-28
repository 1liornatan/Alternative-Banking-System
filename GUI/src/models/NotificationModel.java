package models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class NotificationModel {
    private final StringProperty message;
    private final IntegerProperty yazMade;

    private NotificationModel(NotificationModelBuilder builder) {
        this.message = builder.message;
        this.yazMade = builder.yazMade;
    }

    public String getMessage() {
        return message.get();
    }

    public StringProperty messageProperty() {
        return message;
    }

    public int getYazMade() {
        return yazMade.get();
    }

    public IntegerProperty yazMadeProperty() {
        return yazMade;
    }

    public static class NotificationModelBuilder {
        private final StringProperty message;
        private final IntegerProperty yazMade;

        public NotificationModelBuilder() {
            this.message = new SimpleStringProperty();
            this.yazMade = new SimpleIntegerProperty();
        }

        public NotificationModelBuilder message(String message) {
            this.message.set(message);
            return this;
        }

        public NotificationModelBuilder yazMade(int yazMade) {
            this.yazMade.set(yazMade);
            return this;
        }

        public NotificationModel build() {
            NotificationModel notificationModel = new NotificationModel(this);
            return notificationModel;
        }
    }

}
