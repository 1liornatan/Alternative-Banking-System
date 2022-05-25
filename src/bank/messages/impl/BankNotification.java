package bank.messages.impl;

import bank.messages.Notification;

public class BankNotification implements Notification {
    final String message;
    final int yazMade;

    public BankNotification(String message, int yazMade) {
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
}
