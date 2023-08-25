package faang.school.notificationservice.messageBuilder;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class EventStartEventMessageBuilder implements MessageBuilder {

    private final MessageSource messageSource;

    @Override
    public String buildMessage(Locale locale, String eventName) {
        return messageSource.getMessage("event.start", new Object[]{eventName}, locale);
    }
}
