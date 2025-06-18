package faang.school.notificationservice.messaging;

import faang.school.notificationservice.model.EventType;

import java.util.Locale;

public interface MessageBuilder<T> {

    Class<?> getInstance();

    String buildMessage(T event, Locale locale);

    EventType getEventType();
}
