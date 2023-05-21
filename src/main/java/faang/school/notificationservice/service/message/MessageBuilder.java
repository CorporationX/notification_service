package faang.school.notificationservice.service.message;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;

import java.util.Locale;

@RequiredArgsConstructor
public abstract class MessageBuilder<T> {

    protected final MessageSource messageSource;

    public abstract String getMessage(Locale locale, String... args);
    public abstract Class<T> getEvent();
}
