package faang.school.notificationservice.message;

import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public interface MessageBuilder<T> {

    Class<?> getInstance();

    String buildMessage(T event, Locale locale);
}
