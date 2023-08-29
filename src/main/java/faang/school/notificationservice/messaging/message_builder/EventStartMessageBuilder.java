package faang.school.notificationservice.messaging.message_builder;

import faang.school.notificationservice.dto.EventStartEvent;
import faang.school.notificationservice.model.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class EventStartMessageBuilder implements MessageBuilder<EventStartEvent> {

    private final MessageSource messageSource;

    @Override
    public String buildMessage(EventStartEvent eventType, Locale locale) {
        return messageSource.getMessage("event_start.new", new Object[]{eventType.getTitle()}, locale);
    }

    @Override
    public boolean supportsEventType(EventStartEvent eventType) {
        return eventType.getEventType().equals(EventType.EVENT_STARTED);
    }
}
