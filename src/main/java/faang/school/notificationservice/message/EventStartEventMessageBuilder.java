package faang.school.notificationservice.message;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.EventStartEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventStartEventMessageBuilder implements MessageBuilder<EventStartEventDto> {

    private final MessageSource messageSource;

    @Override
    public String buildMessage(UserDto userDto, EventStartEventDto eventDto) {
        return messageSource.getMessage("event.start", new Object[]{userDto.getUsername(), eventDto.getTitle()}, userDto.getLocale());
    }

    @Override
    public Class<?> getEventType() {
        return EventStartEventDto.class;
    }
}
