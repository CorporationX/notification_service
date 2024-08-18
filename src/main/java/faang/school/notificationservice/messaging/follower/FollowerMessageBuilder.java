package faang.school.notificationservice.messaging.follower;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.event.follower.FollowerEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class FollowerMessageBuilder implements MessageBuilder<FollowerEvent> {

    private final UserServiceClient userServiceClient;
    private final MessageSource source;

    @Override
    public Class<?> getInstance() {
        return null;
    }

    @Override
    public String buildMessage(FollowerEvent event, Locale locale) {
        Long userId = event.getFollowerId();
        UserDto follower = userServiceClient.getUser(userId);

        String message = source.getMessage(
                "follower.new",
                new Object[]{follower.getUsername()},
                locale
        );

        return message;
    }
}
