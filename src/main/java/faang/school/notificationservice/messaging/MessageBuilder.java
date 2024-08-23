package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.UserDto;

import java.util.Locale;

public interface MessageBuilder {

    Class<?> getInstance();

    String buildMessage(UserDto userDto, Locale locale);
}
