package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.context.UserContext;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.FollowerEvent;
import faang.school.notificationservice.event.RecommendationEvent;
import faang.school.notificationservice.listener.EventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

import static faang.school.notificationservice.listener.EventType.EVENT_TYPE_SUBSCRIPTION;

@Slf4j
@Component
@RequiredArgsConstructor
public class FollowerEventMessageBuilder implements MessageBuilder<FollowerEvent> {
    private final MessageSource messageSource;
    private final UserServiceClient userServiceClient;
    private final UserContext userContext;

    @Override
    public EventType getEventType() {
        return EVENT_TYPE_SUBSCRIPTION;
    }

    @Override
    public String buildMessage(FollowerEvent event, Locale locale) {
        userContext.setUserId(event.followeeId());
        log.info("event.followeeId(): {}", event.followeeId());
        UserDto user = userServiceClient.getUser(event.followeeId());
        log.info("Getting user: {}", user);
        return messageSource.getMessage("follower.new",
                new Object[]{user.getUsername()}, locale);
    }
}
