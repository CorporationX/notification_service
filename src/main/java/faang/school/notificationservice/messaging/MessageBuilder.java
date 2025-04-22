package faang.school.notificationservice.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Locale;

public interface MessageBuilder<T> {

    Class<?> getInstance();

    String buildMessage(T event, Locale locale) throws JsonProcessingException;
}
