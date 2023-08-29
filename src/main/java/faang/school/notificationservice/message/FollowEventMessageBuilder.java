package faang.school.notificationservice.message;

import faang.school.notificationservice.dto.FollowerEventDto;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FollowEventMessageBuilder implements MessageBuilder<FollowerEventDto> {

    private final MessageSource messageSource;

    @Override
    public String buildMessage(FollowerEventDto event, UserDto userDto) {
        return messageSource.getMessage("follower.new", new Object[] {userDto.getUsername()}, userDto.getLocale());
    }

    @Override
    public Class<?> getEventType() {
        return null;
    }
}
