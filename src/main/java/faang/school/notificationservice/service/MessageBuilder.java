package faang.school.notificationservice.service;

import java.util.Locale;

public interface MessageBuilder<T> {

    String buildMessage(T event, Locale locale);
    boolean supportsEventType(T event);
}