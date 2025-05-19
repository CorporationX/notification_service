package faang.school.notificationservice.messaging.message2.listener2;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowEventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;

import java.util.List;


public class FollowEventListener extends AbstractNotificationListener<FollowEventDto> {
    private final UserServiceClient userServiceClient;

    public FollowEventListener(
            List<NotificationService> notifications,
            List<MessageBuilder<?>> messageBuilders,
            UserServiceClient userServiceClient
    ) {
        super(notifications, messageBuilders);
        this.userServiceClient = userServiceClient;
    }

    void onMessage(FollowEventDto event) {
        UserDto user = userServiceClient.getUser(event.followeeId());
        handle(user, event, null);
    }
}
