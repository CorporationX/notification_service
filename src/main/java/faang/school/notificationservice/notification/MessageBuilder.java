package faang.school.notificationservice.notification;

import faang.school.notificationservice.dto.UserDto;

import java.util.Map;
import java.util.stream.Stream;

public interface MessageBuilder {

    boolean isApplicable(UserDto userDto);

    Stream<String> apply(Stream<String> stream, UserDto userDto);
    String buildMessage(Map<String, Object> subscriberEvent,Class<?> eventType);
}
