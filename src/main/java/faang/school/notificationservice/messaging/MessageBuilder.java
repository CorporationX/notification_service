package faang.school.notificationservice.messaging;

import faang.school.notificationservice.event.NotificationEvent;

import java.util.Locale;

public interface MessageBuilder<T extends NotificationEvent> {

    Class<T> getInstance();

    String buildMessage(T event, Locale locale);
}
