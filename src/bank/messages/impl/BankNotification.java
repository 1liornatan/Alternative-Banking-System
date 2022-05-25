package bank.messages.impl;

import bank.messages.Notification;

public class BankNotification implements Notification {
    final String message;
    final int yazMade;
    final String ownerId;

    public BankNotification(String ownerId, String message, int yazMade) {
        this.ownerId = ownerId;
        this.message = message;
        this.yazMade = yazMade;
    }

    @Override
    public int getYazMade() {
        return yazMade;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getOwnerId() {
        return ownerId;
    }
}
