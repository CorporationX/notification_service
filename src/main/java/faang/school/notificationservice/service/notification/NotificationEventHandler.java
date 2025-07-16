package faang.school.notificationservice.service.notification;

import faang.school.notificationservice.event.kafka.PostLikedNotificationEvent;

public interface NotificationEventHandler<T> {

    void handle(T event);
}
