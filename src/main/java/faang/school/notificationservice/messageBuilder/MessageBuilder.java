package faang.school.notificationservice.messageBuilder;

import faang.school.notificationservice.dto.UserDto;

public interface MessageBuilder<T> {

    String buildMessage(T event, UserDto userDto);

    Class<?> supportsEventType();
}
