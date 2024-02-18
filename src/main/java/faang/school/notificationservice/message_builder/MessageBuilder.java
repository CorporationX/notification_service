package faang.school.notificationservice.message_builder;

import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public interface MessageBuilder<T> {
    String getMessage(Locale locale, T Event);
}