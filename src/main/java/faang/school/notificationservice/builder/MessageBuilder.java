package faang.school.notificationservice.builder;

import java.util.Locale;

public interface MessageBuilder<T> {

    String buildMessage(T eventType, Locale locale);

    Class<?> supportsEventType();
}
