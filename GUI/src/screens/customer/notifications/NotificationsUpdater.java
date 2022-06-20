package screens.customer.notifications;

import bank.logic.Bank;
import bank.logic.impl.exceptions.DataNotFoundException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.TableView;
import manager.messages.NotificationData;
import models.NotificationModel;

import java.util.ArrayList;
import java.util.List;

public class NotificationsUpdater implements Runnable {

    private final String customerId;
    private final TableView<NotificationModel> notificationsTable;

    public NotificationsUpdater(String customerId, TableView<NotificationModel> notificationsTable) {
        this.customerId = customerId;
        this.notificationsTable = notificationsTable;
    }

    @Override
    public void run() {
        List<NotificationData> notificationsData = null;
        //notificationsData = bankInstance.getNotificationsData(customerId).getNotificationsList();

        List<NotificationModel> notificationModels = new ArrayList<>();

        for (NotificationData data : notificationsData) {
            notificationModels.add(new NotificationModel.NotificationModelBuilder()
                    .message(data.getMessage())
                    .yazMade(data.getYazMade())
                    .build());
        }
        Platform.runLater(() -> {
            notificationsTable.setItems(FXCollections.observableArrayList(notificationModels));
            notificationsTable.sort();
        });
    }
}
