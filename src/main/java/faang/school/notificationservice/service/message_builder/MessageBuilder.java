package faang.school.notificationservice.service.message_builder;

import faang.school.notificationservice.dto.UserDto;

import java.util.Locale;

public interface MessageBuilder<T> {
    String buildMessage (T event, Locale locale);
    long getReceiverId(T event);
    Class<T> eventType ();
}
