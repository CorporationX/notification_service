package faang.school.notificationservice.messaging;

import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public interface MessageBuilder<T> {

    Class<T> getInstance();

    String buildMessage(T event, Locale locale);
}
