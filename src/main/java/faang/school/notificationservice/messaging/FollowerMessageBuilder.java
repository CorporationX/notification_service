package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.FollowerEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class FollowerMessageBuilder implements MessageBuilder<FollowerEvent> {
    private final MessageSource messageSource;
    private final UserServiceClient userClient;

    @Override
    public Class<?> getInstance() {
        return FollowerEvent.class;
    }

    @Override
    public String buildMessage(FollowerEvent event, Locale locale) {
        UserDto follower = userClient.getUser(event.followerId());
        String followerName = follower.getUsername();
        String subscriptionTimestamp = event.timestamp().format(DateTimeFormatter.ofPattern("HH:mm"));

        return messageSource.getMessage(
                "follower.new",
                new Object[]{followerName, subscriptionTimestamp},
                locale
        );
    }
}
