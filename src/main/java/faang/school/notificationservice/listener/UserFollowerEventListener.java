package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.feign.UserServiceClient;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.model.event.UserFollowerEvent;
import faang.school.notificationservice.service.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class UserFollowerEventListener extends AbstractEventListener<UserFollowerEvent> implements MessageListener {

    private final UserServiceClient userServiceClient;
    public UserFollowerEventListener(ObjectMapper objectMapper,
                                     UserServiceClient userServiceClient,
                                     List<NotificationService> notificationServices,
                                     List<MessageBuilder<?>> messageBuilders) {
        super(objectMapper, notificationServices, messageBuilders);
        this.userServiceClient = userServiceClient;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, UserFollowerEvent.class, event -> {
            UserDto followedUserDto = userServiceClient.getUser(event.getFollowedUserId());
            String notificationMessage = buildMessage(event, Locale.UK);
            sendNotification(followedUserDto, notificationMessage);
            log.info("Notification was sent, postAuthorId: {}, notificationMessage: {}", followedUserDto.getId(), notificationMessage);
        });
    }
}