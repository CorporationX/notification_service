package faang.school.notificationservice.service;

import java.util.Locale;

public interface MessageBuilder<T> {
    String buildMessage(T eventType, Locale locale);

    Class<?> supportEventType();

    long getRequestAuthor(T event);
}