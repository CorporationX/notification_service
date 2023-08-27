package faang.school.notificationservice.message;

import faang.school.notificationservice.dto.UserDto;

import java.util.Locale;

public interface MessageBuilder<T> {

    String buildMessage(UserDto userDto, T eventDto);

    Class<?> getEventType();
}
