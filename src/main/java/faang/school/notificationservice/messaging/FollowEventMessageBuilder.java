package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.event.FollowEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class FollowEventMessageBuilder implements MessageBuilder<FollowEvent> {
    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;

    @Override
    public Class<FollowEvent> getInstance() {
        return FollowEvent.class;
    }

    @Override
    public String buildMessage(FollowEvent event, Locale locale) {
        String followerName = userServiceClient.getUser(event.getFollowerId()).getUsername();
        String followeeName = userServiceClient.getUser(event.getFolloweeId()).getUsername();
        return messageSource.getMessage(
                "follower.new",
                new Object[]{followerName, followeeName},
                locale);
    }
}
