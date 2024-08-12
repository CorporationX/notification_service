package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.MessageDto;

import java.util.Locale;

public interface MessageBuilder<T> {

    String buildMessage(T event, MessageDto messageDto, Locale locale);

    Class<?> supportsEventType();
}
