package faang.school.notificationservice.message;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.FollowerEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FollowerMessageBuilder implements MessageBuilder<FollowerEventDto> {

    private final MessageSource messageSource;

    @Override
    public String buildMessage(UserDto userDto, FollowerEventDto eventDto) {
        String message = messageSource.getMessage("follower.new", new String[]{userDto.getUsername()}, userDto.getLocale());
        log.trace("Message for follower notification for user:{} has built successfully", userDto.getUsername());
        return message;
    }

    @Override
    public Class<FollowerEventDto> getEventType() {
        return FollowerEventDto.class;
    }
}
