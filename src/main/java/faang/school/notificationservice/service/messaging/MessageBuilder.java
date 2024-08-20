package faang.school.notificationservice.service.messaging;

import java.util.Locale;

public interface MessageBuilder<T> {

    Class<?> getInstance();

    String getMessage(T event, Locale locale);

    boolean supportEventType(T eventType);
}
