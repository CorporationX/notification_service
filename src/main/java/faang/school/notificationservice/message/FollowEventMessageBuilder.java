package faang.school.notificationservice.message;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.FollowerEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FollowEventMessageBuilder implements MessageBuilder<FollowerEventDto> {

    private final MessageSource messageSource;

    @Override
    public String buildMessage(UserDto userDto, FollowerEventDto eventDto) {
        return messageSource.getMessage("follower.new", new Object[]{userDto.getUsername()}, userDto.getLocale());
    }

    @Override
    public Class<?> getEventType() {
        return FollowerEventDto.class;
    }
}
