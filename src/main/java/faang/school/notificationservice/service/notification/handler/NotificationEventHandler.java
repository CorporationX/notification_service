package faang.school.notificationservice.service.notification.handler;

public interface NotificationEventHandler<T> {

    void saveNotification(T event);
}
