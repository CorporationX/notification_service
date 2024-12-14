package faang.school.notificationservice.messaging;

import java.util.Locale;

public interface MessageBuilder<T> {

    Class<T> getInstance();

    String buildMessage(T event, Locale locale);
}
