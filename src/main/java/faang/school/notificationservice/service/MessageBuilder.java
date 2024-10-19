package faang.school.notificationservice.service;

import java.util.Locale;

public interface MessageBuilder<T> {

    Class<T> getSupportedClass();

    String buildMessage(T event, Locale locale);
}
