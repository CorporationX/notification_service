package faang.school.notificationservice.message;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEvent;
import faang.school.notificationservice.model.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@RequiredArgsConstructor
@Component
public class FollowerMessageBuilder implements MessageBuilder<FollowerEvent> {
    private final UserServiceClient userServiceClient;
    private final MessageSource messageSource;
    @Override
    public String buildMessage(FollowerEvent event, Locale locale) {
        String followerName = userServiceClient.getUser(event.getFollowerId()).getUsername();
        return messageSource.getMessage("follower.new", new String[]{followerName}, locale);
    }
    @Override
    public boolean supportsEventType(FollowerEvent event) {
        return event.getEventType().equals(EventType.FOLLOWER);
    }
}