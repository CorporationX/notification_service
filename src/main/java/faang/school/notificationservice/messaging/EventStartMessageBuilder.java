package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.EventStartEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class EventStartMessageBuilder implements MessageBuilder<EventStartEvent> {

    private final MessageSource messageSource;

    @Override
    public String buildMessage(EventStartEvent eventStartEvent,  Locale locale) {
        Object[] event_id = { eventStartEvent.getEvent_id() };
        return messageSource.getMessage("event_start.new", event_id, locale);
    }

    @Override
    public boolean supportsEventType(Class<?> eventType) {
        return eventType.equals(EventStartEvent.class);
    }
}


