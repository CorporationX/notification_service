package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.model.event.CommentEvent;
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
public class CommentEventListener extends AbstractEventListener<CommentEvent> implements MessageListener {

    private final UserServiceClient userServiceClient;

    public CommentEventListener(ObjectMapper objectMapper,
                                List<NotificationService> notificationServices,
                                UserServiceClient userServiceClient,
                                List<MessageBuilder<?>> messageBuilders) {
        super(objectMapper, notificationServices, messageBuilders);
        this.userServiceClient = userServiceClient;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, CommentEvent.class, event -> {
            UserDto postAuthorDto = userServiceClient.getUser(event.getPostAuthorId());
            String notificationMessage = buildMessage(event, Locale.ENGLISH);
            sendNotification(postAuthorDto, notificationMessage);
            log.info("Notification was sent, postAuthorId: {}, notificationMessage: {}", postAuthorDto.getId(), notificationMessage);
        });
    }
}
