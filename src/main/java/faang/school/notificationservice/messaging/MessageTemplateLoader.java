package faang.school.notificationservice.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MessageTemplateLoader {

    private final MessageSource messageSource;

    public String getTemplate(String key, Locale locale) {
        return messageSource.getMessage(key, null, locale);
    }
}
