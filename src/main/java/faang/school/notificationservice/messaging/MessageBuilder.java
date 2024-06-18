package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.NotificationData;

import java.util.Locale;

public interface MessageBuilder<T> {

    Class<?> getInstance();

    String buildMessage(T event, Locale locale, NotificationData notificationData);
}
