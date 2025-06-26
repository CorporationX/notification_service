package faang.school.notificationservice.messaging;

import faang.school.notificationservice.event.Event;

import java.util.Locale;

public interface MessageBuilder<T extends Event> {

    Class<T> supportsEventType();

    String buildMessage(T event, Locale locale);
}
