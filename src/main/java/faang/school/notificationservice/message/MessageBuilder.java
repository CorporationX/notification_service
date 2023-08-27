package faang.school.notificationservice.message;

import faang.school.notificationservice.dto.UserDto;

public interface MessageBuilder<T> {

    public String buildMessage(UserDto userDto, T eventDto);

    public Class<T> getEventType();
}
