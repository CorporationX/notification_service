package faang.school.notificationservice.messaging;

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
        return buildEventStartMessage(event.getEventId());
    }

    public String buildEventStartMessage(Long eventId) {
        return "Event with ID " + eventId + " has just started!";
    }
}
