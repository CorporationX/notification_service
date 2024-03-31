package faang.school.notificationservice.message_builder;

import java.util.Locale;

public interface MessageBuilder<T> {

    String buildMessage(T eventType, Locale locale);

    Class<?> supportsEventType();
}
