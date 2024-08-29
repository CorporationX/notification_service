package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.FollowerEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class FollowerEventListener extends AbstractEventListener<FollowerEvent> {

    public FollowerEventListener(ObjectMapper objectMapper,
                                 List<NotificationService> notificationServices,
                                 MessageBuilder<FollowerEvent> messageBuilder,
                                 UserService userService) {
        super(objectMapper, notificationServices, messageBuilder, userService);
    }

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        FollowerEvent event = handleEvent(FollowerEvent.class, message);
        UserDto followee = userService.getUser(event.getFolloweeId());
        UserDto follower = userService.getUser(event.getFollowerId());
        Object[] additionData = {};
        handleNotification(followee, follower, additionData);
    }
}