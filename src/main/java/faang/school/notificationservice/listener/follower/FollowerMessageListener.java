package faang.school.notificationservice.listener.follower;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.event.follower.FollowerEvent;
import faang.school.notificationservice.listener.AbstractMessageListener;
import faang.school.notificationservice.messaging.follower.FollowerMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.user.UserService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class FollowerMessageListener extends AbstractMessageListener<FollowerEvent> {

    private final FollowerMessageBuilder followerMessageBuilder;
    private final List<NotificationService> notificationServices;
    private final UserService userService;

    public FollowerMessageListener(ObjectMapper objectMapper,
                                   FollowerMessageBuilder followerMessageBuilder,
                                   List<NotificationService> notificationServices,
                                   UserService userService) {
        super(objectMapper);
        this.followerMessageBuilder = followerMessageBuilder;
        this.notificationServices = notificationServices;
        this.userService = userService;
    }

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        handleEvent(FollowerEvent.class, message, event -> {
            try {
                UserDto followeeUser = userService.getUser(event.getFolloweeId());
                UserDto followerUser = userService.getUser(event.getFollowerId());

                String messageText = followerMessageBuilder.buildMessage(followerUser, Locale.getDefault());

                notificationServices.stream()
                        .filter(service -> service.getPreferredContact().equals(followeeUser.getPreference()))
                        .findFirst()
                        .ifPresentOrElse(
                                service -> service.send(followeeUser, messageText),
                                () -> log.warn("No matching notification service found for user preference: {}",
                                        followeeUser.getPreference())
                        );
            } catch (Exception e) {
                log.error("Failed to process follower event for followeeId: {} and followerId: {}",
                        event.getFolloweeId(), event.getFollowerId(), e);
            }
        });
    }
}
