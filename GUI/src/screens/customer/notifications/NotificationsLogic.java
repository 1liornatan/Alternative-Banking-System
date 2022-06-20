package screens.customer.notifications;

import javafx.beans.property.StringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.NotificationModel;
import screens.customer.constants.Constants;

public class NotificationsLogic {

    private final StringProperty customerId;
    private final TableView<NotificationModel> notificationsTable;

    public NotificationsLogic(StringProperty customerId, TableView<NotificationModel> notificationsTable) {
        this.customerId = customerId;
        this.notificationsTable = notificationsTable;
        setNotificationsTable();
    }

    private void setNotificationsTable() {
        TableColumn<NotificationModel, String> notificationMessageColumn = new TableColumn<>("Message");
        notificationMessageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
        notificationMessageColumn.setPrefWidth(300);
        TableColumn<NotificationModel, Integer> yazMadeColumn = new TableColumn<>("Date");
        yazMadeColumn.setCellValueFactory(new PropertyValueFactory<>("yazMade"));

        yazMadeColumn.maxWidthProperty().bind(notificationsTable.widthProperty().multiply(0.1));
        yazMadeColumn.setStyle("-fx-alignment: CENTER;");
        notificationMessageColumn.maxWidthProperty().bind(notificationsTable.widthProperty().multiply(0.9));
        notificationsTable.getColumns().addAll(notificationMessageColumn, yazMadeColumn);
        yazMadeColumn.setSortType(TableColumn.SortType.DESCENDING);
        notificationsTable.getSortOrder().clear();
        notificationsTable.getSortOrder().add(yazMadeColumn);

    }

    public void updateNotifications() {
        new Thread(new NotificationsUpdater(customerId.get(), notificationsTable), Constants.THREAD_NOTIFICATIONS).start();
    }
}
