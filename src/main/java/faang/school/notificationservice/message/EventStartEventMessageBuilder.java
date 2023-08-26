package faang.school.notificationservice.message;

import faang.school.notificationservice.dto.EventStartEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class EventStartEventMessageBuilder implements MessageBuilder<EventStartEventDto> {

    private final MessageSource messageSource;

    @Override
    public String buildMessage(EventStartEventDto eventStartEventDto, Locale locale) {
        return messageSource.getMessage("event.start", new Object[]{eventName}, locale);
    }

    @Override
    public Class<?> supportsEventType() {
        return EventStartEventDto.class;
    }
}
