package faang.school.notificationservice.messaging.message2.listener2;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowEventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;

import java.util.List;


public class FollowEventListener extends AbstractNotificationListener<FollowEventDto> {
    private final UserServiceClient userServiceClient;
    private final MessageBuilder<FollowEventDto> messageBuilder;

    public FollowEventListener(
            List<NotificationService> notifications,
            UserServiceClient userServiceClient,
            MessageBuilder<FollowEventDto> messageBuilder
    ) {
        super(notifications);
        this.userServiceClient = userServiceClient;
        this.messageBuilder = messageBuilder;
    }

    void onMessage(FollowEventDto event) {
        UserDto user = userServiceClient.getUser(event.followeeId());
        handle(user, event,messageBuilder,null);
    }
}
