package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.MessageDto;

import faang.school.notificationservice.dto.UserDto;

import java.util.Locale;

public interface MessageBuilder<T> {

    String buildMessage(T event, MessageDto messageDto, Locale locale);
    String buildMessage(UserDto notifiedUser, Locale locale);

    Class<?> supportsEventType();
    T getEvent(T t);
}
