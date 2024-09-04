package faang.school.notificationservice.listener.follower;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.event.follower.FollowerEvent;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Добавлено для примера
 * */

@Service
public class FollowerListener extends AbstractEventListener<FollowerEvent> {

    private final UserServiceClient userServiceClient;

    FollowerListener(
            List<NotificationService> notificationServices,
            ObjectMapper mapper,
            MessageBuilder<FollowerEvent> messageBuilder,
            UserServiceClient userServiceClient
    ) {
        super(notificationServices, mapper, messageBuilder, FollowerEvent.class);
        this.userServiceClient = userServiceClient;
    }

    @Override
    public List<UserDto> getNotifiedUsers(FollowerEvent event) {
        UserDto followeeUser = userServiceClient.getUser(
                event.getFolloweeId()
        );
        return List.of(followeeUser);
    }
}
