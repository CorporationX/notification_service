package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.UserDto;

import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

public interface MessageBuilder {

    String buildMessage(Class<?> eventType, Locale locale, String... args);
    String buildMessage(Map<String, Object> subscriberEvent,Class<?> eventType);

    boolean supportsEventType(Class<?> eventType);
}
