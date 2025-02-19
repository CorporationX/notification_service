package faang.school.notificationservice.messaging;

import faang.school.notificationservice.listener.EventType;

import java.util.Locale;

public interface MessageBuilder<T> {

    EventType getEventType();

    String buildMessage(T event, Locale locale);
}
