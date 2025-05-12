package faang.school.notificationservice.messaging.message2.builder2;

import faang.school.notificationservice.messaging.MessageBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;

import java.util.Locale;


@Getter(AccessLevel.PROTECTED)
public abstract class AbstractMessageBuilder<T> implements MessageBuilder<T> {
    private final Locale defaultLocale;
    private final MessageSource messageSource;

    public AbstractMessageBuilder(@Value("${app.default-locale}") Locale defaultLocale, MessageSource messageSource) {
        this.defaultLocale = defaultLocale;
        this.messageSource = messageSource;
    }

    @Override
    public final String buildMessage(T event, Locale locale) {
        Locale effectiveLocale = locale != null ? locale : defaultLocale;
        return buildMessageWithLocale(event, effectiveLocale);
    }

    protected abstract String buildMessageWithLocale(T event, Locale locale);
}
