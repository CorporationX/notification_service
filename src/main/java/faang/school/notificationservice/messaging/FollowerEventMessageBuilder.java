package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.FollowerEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class FollowerEventMessageBuilder implements MessageBuilder<FollowerEventDto> {

    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;
    private static final String EVENT_NEW_FOLLOWER = "event.newFollower";

    @Override
    public Class<?> getInstance() {
        return FollowerEventDto.class;
    }

    @Override
    public String buildMessage(FollowerEventDto event, Locale locale) {
        UserDto follower = userServiceClient.getUser(event.getFollowerId());
        String followerName = follower != null ? follower.getUsername() : "Unknown User";
        return messageSource.getMessage(EVENT_NEW_FOLLOWER, new Object[]{followerName}, locale);
    }
}