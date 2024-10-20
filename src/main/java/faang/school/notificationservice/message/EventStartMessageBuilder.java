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
    public Class<?> getInstance() {
        return EventStartEvent.class;
    }

    @Override
    public String buildMessage(EventStartEvent event, Locale locale) {
        String eventName = event.getTitle();
        return messageSource.getMessage("event.start", new Object[]{eventName}, locale);
    }
}
