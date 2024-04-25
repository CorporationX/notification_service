package faang.school.notificationservice.messages;

import java.util.Locale;

public interface MessageBuilder<T> {

    boolean supportsEventType(Class<?> eventType);
    String buildMessage(T event, Locale locale);
}