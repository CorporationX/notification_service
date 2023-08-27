package faang.school.notificationservice.messageBuilder;

import java.util.Locale;

public interface MessageBuilder<T> {

    String buildMessage(T event, Locale locale);

    Class<?> supportsEventType();
}
