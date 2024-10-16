package faang.school.notificationservice.listener.follower;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.event.follower.FollowerEvent;
import faang.school.notificationservice.listener.AbstractMessageListener;
import faang.school.notificationservice.messaging.follower.FollowerMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;

@Slf4j
@Component
public class FollowerMessageListener extends AbstractMessageListener<FollowerEvent> {

    private final FollowerMessageBuilder followerMessageBuilder;
    private final UserService userService;

    public FollowerMessageListener(ObjectMapper objectMapper,
                                   FollowerMessageBuilder followerMessageBuilder,
                                   Map<UserDto.PreferredContact, NotificationService> notificationServices,
                                   UserService userService) {
        super(objectMapper, notificationServices);
        this.followerMessageBuilder = followerMessageBuilder;
        this.userService = userService;
    }

    @Override
    protected Class<FollowerEvent> getEventClass() {
        return FollowerEvent.class;
    }

    @Override
    protected void handleEvent(FollowerEvent event) {
        try {
            UserDto followeeUser = userService.getUser(event.getFolloweeId());
            UserDto followerUser = userService.getUser(event.getFollowerId());
            String messageText = followerMessageBuilder.buildMessage(followerUser, Locale.getDefault());

            sendNotification(followeeUser, messageText);
        } catch (Exception e) {
            log.error("Failed to process follower event for followeeId: {} and followerId: {}",
                    event.getFolloweeId(), event.getFollowerId(), e);
        }
    }
}
