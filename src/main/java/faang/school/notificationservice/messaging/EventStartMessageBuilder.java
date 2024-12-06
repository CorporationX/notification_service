package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.EventStartEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class EventStartMessageBuilder implements MessageBuilder<EventStartEventDto>{

    private final MessageSource messageSource;

    @Override
    public Class<EventStartEventDto> supportEventType() {
        return EventStartEventDto.class;
    }

    @Override
    public String buildMessage(EventStartEventDto event, Locale locale) {
        String message = "event." + event.getTimeBeforeStart();
        return messageSource.getMessage(message, new Object[]{event.getEventId()}, locale);
    }
}
