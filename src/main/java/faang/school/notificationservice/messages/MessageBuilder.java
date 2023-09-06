package faang.school.notificationservice.messages;

import java.util.Locale;

public interface MessageBuilder<T> {

    String buildMessage(T event, Locale locale);

    boolean supportsEventType(Object eventType);
}
