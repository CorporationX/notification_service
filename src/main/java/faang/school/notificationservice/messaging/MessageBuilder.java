package faang.school.notificationservice.messaging;

import java.util.Locale;

public interface MessageBuilder<T> {
    Class<T> getEventType();
    String buildMessage(T event, Locale locale);
}