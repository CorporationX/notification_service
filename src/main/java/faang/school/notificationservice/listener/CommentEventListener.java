package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.CommentEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class CommentEventListener extends AbstractEventListener<CommentEvent> implements MessageListener {

    public CommentEventListener(ObjectMapper objectMapper,
                                List<NotificationService> notificationServices,
                                MessageBuilder<CommentEvent> messageBuilder,
                                UserService userService) {
        super(objectMapper, notificationServices,
                messageBuilder, userService);
    }

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        CommentEvent event = handleEvent(CommentEvent.class, message);
        UserDto commentAuthor = userService.getUser(event.getCommentAuthorId());
        UserDto postAuthor = userService.getUser(event.getPostAuthorId());
        Object[] additionData = {};
        handleNotification(postAuthor, commentAuthor, additionData);
    }
}
