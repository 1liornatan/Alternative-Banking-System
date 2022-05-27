package manager.messages;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class NotificationData {
    private final StringProperty message;
    private final IntegerProperty yazMade;

    private NotificationData(NotificationDataBuilder builder) {
        this.message = builder.message;
        this.yazMade = builder.yazMade;
    }

    public StringProperty getMessage() {
        return message;
    }

    public IntegerProperty getYazMade() {
        return yazMade;
    }

    public static class NotificationDataBuilder {
        private final StringProperty message;
        private final IntegerProperty yazMade;

        public NotificationDataBuilder() {
            this.message = new SimpleStringProperty();
            this.yazMade = new SimpleIntegerProperty();
        }

        public NotificationDataBuilder message(String message) {
            this.message.set(message);
            return this;
        }

        public NotificationDataBuilder yazMade(int yazMade) {
            this.yazMade.set(yazMade);
            return this;
        }

        public NotificationData build() {
            NotificationData notificationData = new NotificationData(this);
            return notificationData;
        }
    }

}
