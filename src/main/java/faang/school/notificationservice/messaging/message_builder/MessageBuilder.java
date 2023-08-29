package faang.school.notificationservice.messaging.message_builder;

import java.util.Locale;

public interface MessageBuilder<T> {

    String buildMessage(T eventType, Locale locale);
    boolean supportsEventType(T eventType);
}
