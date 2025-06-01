package faang.school.notificationservice.messaging;

import faang.school.notificationservice.messaging.event.EventStartEvent;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class EventStartMessageBuilder implements MessageBuilder<EventStartEvent> {

    @Override
    public Class<?> getInstance() {
        return EventStartEvent.class;
    }

    @Override
    public String buildMessage(EventStartEvent event, Locale locale) {
        // Строим сообщение для события
        return "Событие " + event.getEventId() + " начинается прямо сейчас!";
    }
}
