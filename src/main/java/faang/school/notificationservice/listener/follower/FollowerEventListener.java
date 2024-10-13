package faang.school.notificationservice.listener.follower;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.event.follower.FollowerEvent;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.messaging.follower.FollowerMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.user.UserService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class FollowerEventListener extends AbstractEventListener<FollowerEvent> implements MessageListener {

    private final FollowerMessageBuilder followerMessageBuilder;
    private final List<NotificationService> notificationServices;
    private final UserService userService;

    public FollowerEventListener(ObjectMapper objectMapper,
                                 FollowerMessageBuilder followerMessageBuilder,
                                 List<NotificationService> notificationServices, UserService userService) {
        super(objectMapper);
        this.followerMessageBuilder = followerMessageBuilder;
        this.notificationServices = notificationServices;
        this.userService = userService;
    }

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        handleEvent(FollowerEvent.class, message, (event) -> {
            UserDto followeeUser = userService.getUser(event.getFolloweeId());
            UserDto followerUser = userService.getUser(event.getFollowerId());
            String messageText = followerMessageBuilder.buildMessage(followerUser, Locale.getDefault());

            notificationServices.stream()
                    .filter(service -> service.getPreferredContact().equals(followeeUser.getPreference()))
                    .findFirst()
                    .ifPresent(service -> service.send(followeeUser, messageText));
        });
    }
}
