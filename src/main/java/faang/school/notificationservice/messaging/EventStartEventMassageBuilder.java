package faang.school.notificationservice.messaging;

import faang.school.notificationservice.event.kafka.EventStartNotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class EventStartEventMassageBuilder implements MessageBuilder<EventStartNotificationEvent> {

    private final MessageSource messageSource;

    @Override
    public Class<?> getInstance() {
        return EventStartNotificationEvent.class;
    }

    @Override
    public String buildMessage(EventStartNotificationEvent event, Locale locale) {
        return messageSource.getMessage(
                "event.started",
                new Object[]{event.getEventTitle()},
                locale);
    }
}
