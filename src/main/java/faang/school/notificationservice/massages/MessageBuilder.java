package faang.school.notificationservice.massages;

import java.util.Locale;

public interface MessageBuilder<T> {

    String buildMessage(T event, Locale locale);

    boolean supportsEventType(Object eventType);
}
