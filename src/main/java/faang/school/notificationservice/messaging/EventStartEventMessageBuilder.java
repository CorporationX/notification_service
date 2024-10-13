package faang.school.notificationservice.messaging;

import faang.school.notificationservice.messaging.dto.EventStartDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@RequiredArgsConstructor
@Component
public class EventStartEventMessageBuilder implements MessageBuilder<EventStartDto> {
    private static final String EVENT_STARTED_CODE = "event.started";

    private final MessageSource messageSource;

    @Override
    public Class<EventStartDto> getInstance() {
        return EventStartDto.class;
    }

    @Override
    public String buildMessage(EventStartDto event, Locale locale) {
        Object[] args = {event.getAttendeeName(), event.getEventTitle()};
        return messageSource.getMessage(EVENT_STARTED_CODE, args, locale);
    }
}
