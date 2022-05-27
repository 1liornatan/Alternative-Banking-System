package manager.messages;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class NotificationData {
    private final String message;
    private final int yazMade;

    private NotificationData(NotificationDataBuilder builder) {
        this.message = builder.message;
        this.yazMade = builder.yazMade;
    }

    public String getMessage() {
        return message;
    }

    public int getYazMade() {
        return yazMade;
    }

    public static class NotificationDataBuilder {
        private String message;
        private int yazMade;

        public NotificationDataBuilder() {
            this.message = message;
            this.yazMade = yazMade;
        }

        public NotificationDataBuilder message(String message) {
            this.message = message;
            return this;
        }

        public NotificationDataBuilder yazMade(int yazMade) {
            this.yazMade = yazMade;
            return this;
        }

        public NotificationData build() {
            NotificationData notificationData = new NotificationData(this);
            return notificationData;
        }
    }

}
