package bank.messages.impl;

import bank.messages.Notification;

public class BankNotification implements Notification {
    final String message;
    final String ownerId;

    public BankNotification(String ownerId, String message) {
        this.ownerId = ownerId;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getId() {
        return ownerId;
    }
}
