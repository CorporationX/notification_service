package faang.school.notificationservice.message;

import faang.school.notificationservice.dto.EventStartEventDto;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventStartEventMessageBuilder implements MessageBuilder<EventStartEventDto> {

    private final MessageSource messageSource;

    @Override
    public String buildMessage(EventStartEventDto eventStartEventDto, UserDto userDto) {
        String eventName = eventStartEventDto.getTitle();
        return messageSource.getMessage("event.start", new Object[]{eventName}, userDto.getLocale());
    }

    @Override
    public Class<?> getEventType() {
        return EventStartEventDto.class;
    }
}
