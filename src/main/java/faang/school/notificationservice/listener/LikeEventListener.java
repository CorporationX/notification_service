package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.publishable.LikeEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LikeEventListener extends AbstractEventListenerByDima<LikeEvent> {
    private final UserServiceClient userServiceClient;

    public LikeEventListener(List<NotificationService> notifiers,
                             ObjectMapper mapper,
                             MessageBuilder<LikeEvent> messageBuilder,
                             UserServiceClient userServiceClient) {
        super(notifiers, mapper, messageBuilder, LikeEvent.class);
        this.userServiceClient = userServiceClient;
    }

    @Override
    protected List<UserDto> getNotifiedUsers(LikeEvent event) {
        UserDto notifiedUser = userServiceClient.getUser(event.getReceiverId());
        return List.of(notifiedUser);
    }

    @Override
    protected Object[] getArgs(LikeEvent event) {
        String receiverName = userServiceClient.getUser(event.getReceiverId()).getUsername();
        String likerName = userServiceClient.getUser(event.getActorId()).getUsername();
        return new Object[]{receiverName, likerName};
    }
}
