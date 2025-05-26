package faang.school.notificationservice.messaging.messagebuilder;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;

import java.util.Locale;


@Getter(AccessLevel.PROTECTED)
public abstract class AbstractMessageBuilder<T> implements MessageBuilder<T> {
    private final Locale defaultLocale;
    private final MessageSource messageSource;

    public AbstractMessageBuilder(String defaultLocaleString, MessageSource messageSource) {
        this.defaultLocale =  Locale.forLanguageTag(defaultLocaleString);
        this.messageSource = messageSource;
    }

    @Override
    public final String buildMessage(T event, Locale locale) {
        Locale effectiveLocale = locale != null ? locale : defaultLocale;
        return buildMessageWithLocale(event, effectiveLocale);
    }

    protected abstract String buildMessageWithLocale(T event, Locale locale);
}
