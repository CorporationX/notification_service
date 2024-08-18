package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.FollowerEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.FollowerMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Slf4j
@Service
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
        FollowerEvent event = handleEvent(FollowerEvent.class, message);
        UserDto followee = userService.getUser(event.getFolloweeId());
        UserDto follower = userService.getUser(event.getFollowerId());
        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact()
                        .equals(UserDto.PreferredContact.EMAIL))
                .findFirst()
                .ifPresent(notificationService ->
                        notificationService.send(followee, followerMessageBuilder
                                .buildMessage(follower, Locale.getDefault())));
    }
}
