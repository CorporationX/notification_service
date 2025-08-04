package faang.school.notificationservice.service.notification.handler;

import java.util.List;

public interface NotificationEventHandler<T> {
    void saveNotifications(List<T> events);
}
