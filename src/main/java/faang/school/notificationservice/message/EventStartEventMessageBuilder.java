package faang.school.notificationservice.message;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.EventStartEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class EventStartEventMessageBuilder implements MessageBuilder<EventStartEventDto> {

    private final MessageSource messageSource;

    @Override
    public String buildMessage(UserDto userDto, EventStartEventDto event) {
        return messageSource.getMessage("event.start", new Object[]{userDto.getUsername(), event.getTitle()}, Locale.UK);
    }

    @Override
    public Class<EventStartEventDto> getEventType() {
        return EventStartEventDto.class;
    }
}
