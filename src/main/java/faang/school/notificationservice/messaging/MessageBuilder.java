package faang.school.notificationservice.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;

import java.util.Locale;

@RequiredArgsConstructor
public abstract class MessageBuilder<T> {
    private final MessageSource messageSource;

    public abstract Class<?> getInstance();

    public abstract String buildMessage(T event, Locale locale);

    protected String buildMessage(String code, Locale locale, String... args) {
        return messageSource.getMessage(code, args, locale);
    }
}
