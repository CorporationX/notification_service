package faang.school.notificationservice.messaging;

import java.util.Locale;

public interface MessageBuilder<T> {

    String buildMessage(T eventType, Locale locale);

    Class<?> supportsEventType();

}
