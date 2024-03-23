package faang.school.notificationservice.message;

import faang.school.notificationservice.dto.UserDto;

public interface MessageBuilder<T> {

    String buildMessage(UserDto userDto, T event);

    Class<T> getEventType();
}
