package faang.school.notificationservice.service.message_builder;

import java.util.Locale;

public interface MessageBuilder<T> {

    String buildMessage(T eventType, Locale locale);

    long getReceiverId(T t);
}
