package faang.school.notificationservice.notification;

import java.util.Locale;

public interface MessageBuilder<T> {

    String buildMessage(T eventType, Locale locale);

    Class<?> supportEventType();
}

