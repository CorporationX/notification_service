package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.UserDto;

import java.util.Locale;

public interface MessageBuilder<T> {

    String buildMessage(UserDto notifiedUser, Locale locale);

    T getEvent(T t);
}
