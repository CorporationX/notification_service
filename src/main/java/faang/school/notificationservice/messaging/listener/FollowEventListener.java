package faang.school.notificationservice.messaging.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowEventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.messagebuilder.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class FollowEventListener extends AbstractNotificationListener<FollowEventDto> {
    private final UserServiceClient userServiceClient;

    public FollowEventListener(
            List<NotificationService> notifications,
            List<? extends MessageBuilder<? extends FollowEventDto>> messageBuilders,
            UserServiceClient userServiceClient
    ) {
        super(notifications, messageBuilders);
        this.userServiceClient = userServiceClient;
    }

    public void onMessage1(FollowEventDto event) {
        log.info("+++++++++++ Method onMessage1 was invoked");
        var user = userServiceClient.getUser(event.followeeId());
        System.out.println("------------------"+user.getClass());

        handle(user, event, null);
    }

    public void onMessage(Object message) {
        log.info("+++++++++++ Raw message received: {}", message);

    }

}
