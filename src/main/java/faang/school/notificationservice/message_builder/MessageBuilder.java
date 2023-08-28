package faang.school.notificationservice.message_builder;

import faang.school.notificationservice.dto.notification.NotificationData;

import java.util.Locale;

public interface MessageBuilder<T> {
    String buildMessage(NotificationData data, Locale locale);

    boolean supportsEventType(T eventType);
}
