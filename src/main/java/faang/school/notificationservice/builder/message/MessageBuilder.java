package faang.school.notificationservice.builder.message;

import faang.school.notificationservice.message.event.NotificationEvent;

import java.util.Locale;

public interface MessageBuilder<T extends NotificationEvent> {
    String build(T event, Locale locale);
}
