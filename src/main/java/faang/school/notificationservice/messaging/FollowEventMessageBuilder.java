package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowEventDto;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class FollowEventMessageBuilder implements MessageBuilder<FollowEventDto> {
    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;

    @Override
    public Class<?> getInstance() {
        return FollowEventDto.class;
    }

    @Override
    public String buildMessage(FollowEventDto event, Locale locale) {
        UserDto user = userServiceClient.getUser(event.followerId());
        return messageSource.getMessage(
                "follow.notification",
                new Object[]{user.getUsername()},
                locale);
    }
}
