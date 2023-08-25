package faang.school.notificationservice.message;

import faang.school.notificationservice.dto.event.EventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.EventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FollowerMessageBuilder implements MessageBuilder {

    private final MessageSource messageSource;

    @Override
    public String buildMessage(UserDto userDto, EventDto eventDto) {
        String message = messageSource.getMessage("follower.new", new String[]{userDto.getUsername()}, userDto.getLocale());
        log.info("Message for follower notification for user:{} has built successfully", userDto.getUsername());
        return message;
    }

    public EventType getEventType() {
        return EventType.FOLLOWER_EVENT;
    }
}
