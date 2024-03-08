package faang.school.notificationservice.messages.builder;

import java.util.Locale;

public interface MessageBuilder<T> {

    String builderMessage(T eventType, Locale locale);

    Class<?> supportsEventType();
}
