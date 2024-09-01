package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.CommentEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.CommentEventMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class CommentEventListener extends AbstractListener<CommentEvent> {

    private final UserServiceClient userServiceClient;

    public CommentEventListener(ObjectMapper objectMapper,
                                List<NotificationService> notificationServices,
                                CommentEventMessageBuilder commentEventMessageBuilder, UserServiceClient userServiceClient) {
        super(objectMapper, userServiceClient, notificationServices, commentEventMessageBuilder, CommentEvent.class);
        this.userServiceClient = userServiceClient;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        CommentEvent event = objectMapper.convertValue(message.getBody(), CommentEvent.class);
        log.info("Received comment event: {}", event);
        
    }

    @Override
    public List<UserDto> getUsersToNotify(CommentEvent event) {
        List<UserDto> users = new ArrayList<>();
        UserDto user = userServiceClient.getUser(event.getPostAuthorId());
        users.add(user);
        return users;
    }

}
