package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEvent;
import faang.school.notificationservice.dto.UserProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class FollowerMessageBuilder implements MessageBuilder<FollowerEvent> {
    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;

    @Override
    public String buildMessage(FollowerEvent event, Locale locale) {
        UserProfileDto follower = userServiceClient.getUserProfile(event.getFollowerId());
        return messageSource.getMessage(
                "notification.follower.new",
                new Object[]{follower.getUsername()},
                locale
        );
    }

    @Override
    public Class<FollowerEvent> getEventType() {
        return FollowerEvent.class;
    }
}