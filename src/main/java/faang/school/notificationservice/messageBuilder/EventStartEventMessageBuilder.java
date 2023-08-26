package faang.school.notificationservice.messageBuilder;

import faang.school.notificationservice.dto.EventCountdown;
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
        return null;
    }

    public String buildCustomMessage(Locale locale, EventCountdown eventCountdown, String eventTitle) {
        String message = "event.countdown." + eventCountdown;
        return messageSource.getMessage(message, new Object[]{eventTitle}, locale);
    }
}
