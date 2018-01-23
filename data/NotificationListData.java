package com.mawaqaa.eatandrun.data;

/**
 * Created by HP on 7/10/2017.
 */

public class NotificationListData {

    private String NotificationId;
    private String NotificationDescription;
    private String LastModifiedDate;

    public NotificationListData(String notificationId, String notificationDescription, String lastModifiedDate) {
        NotificationId = notificationId;
        NotificationDescription = notificationDescription;
        LastModifiedDate = lastModifiedDate;
    }

    public void setNotificationId(String notificationId) {
        NotificationId = notificationId;
    }

    public void setNotificationDescription(String notificationDescription) {
        NotificationDescription = notificationDescription;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        LastModifiedDate = lastModifiedDate;
    }

    public String getNotificationId() {
        return NotificationId;
    }

    public String getNotificationDescription() {
        return NotificationDescription;
    }

    public String getLastModifiedDate() {
        return LastModifiedDate;
    }
}
