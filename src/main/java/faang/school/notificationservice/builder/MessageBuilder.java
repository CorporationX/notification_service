package faang.school.notificationservice.builder;

import faang.school.notificationservice.dto.EmailEvent;

import java.util.Locale;

public interface MessageBuilder<T> {
    String buildMessage(T event, Locale locale);
    Class<?> supportsEventType();
}
