package manager.messages;

import java.util.List;

public class NotificationsData {
    private List<NotificationData> notificationsList;
    private int notificationVersion;

    public List<NotificationData> getNotificationsList() {
        return notificationsList;
    }

    public void setNotificationsList(List<NotificationData> notificationsList) {
        this.notificationsList = notificationsList;
    }

    public int getNotificationVersion() {
        return notificationVersion;
    }

    public void setNotificationVersion(int notificationVersion) {
        this.notificationVersion = notificationVersion;
    }
}
