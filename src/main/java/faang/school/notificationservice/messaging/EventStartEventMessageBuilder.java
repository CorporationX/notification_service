package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.EventServiceClient;
import faang.school.notificationservice.dto.event.EventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class EventStartEventMessageBuilder implements MessageBuilder<EventDto> {

    private final MessageSource messageSource;
    private final EventServiceClient eventServiceClient;

    @Override
    public String buildMessage(EventDto event, Locale locale) {
        EventDto eventDto = eventServiceClient.getEvent(event.eventId());
        return messageSource.getMessage("event.start",
                new Object[]{eventDto.title()}, locale);
    }
}