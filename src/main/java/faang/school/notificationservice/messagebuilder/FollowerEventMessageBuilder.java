package faang.school.notificationservice.messagebuilder;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.event.follower.FollowerEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class FollowerEventMessageBuilder implements MessageBuilder<FollowerEvent> {
    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;

    @Override
    public String buildMessage(FollowerEvent event, Locale locale) {
        String[] userNames = {userServiceClient.getUser(event.getFolloweeId()).getUsername(),
                              userServiceClient.getUser(event.getFollowerId()).getUsername()};
        return messageSource.getMessage("follower.new", userNames, Locale.ENGLISH);
    }

    @Override
    public Class<?> getEventType() {
        return FollowerEvent.class;
    }
}
