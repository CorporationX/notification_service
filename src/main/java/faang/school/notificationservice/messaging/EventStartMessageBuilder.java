package faang.school.notificationservice.messaging;

import faang.school.notificationservice.model.event.EventStartEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class EventStartMessageBuilder implements MessageBuilder<EventStartEvent> {

    private final MessageSource messageSource;

    @Override
    public String buildMessage(EventStartEvent event, Locale locale) {
        return messageSource.getMessage("event.start", null, locale);
    }
}
