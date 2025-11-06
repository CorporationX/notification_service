package faang.school.notificationservice.messaging.message_builder;

import java.util.Locale;

public interface MessageBuilder<T> {

    Class<?> getInstance();

    String buildMessage(T event, Locale locale);
}
