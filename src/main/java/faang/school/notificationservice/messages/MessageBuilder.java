package faang.school.notificationservice.messages;

import java.util.Locale;

public interface MessageBuilder<T> {
    String buildMessage(T event, Locale userLocale);

    boolean supportsEventType(Class<?> eventType);
//    Class<?> supportsEventType();
}
