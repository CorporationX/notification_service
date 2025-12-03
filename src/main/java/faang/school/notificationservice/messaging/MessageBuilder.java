package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.UserDto;

import java.util.Locale;

public interface MessageBuilder<T> {

    Class<T> getEventType();

    String buildMessage(T event, UserDto author, Locale locale);
}
