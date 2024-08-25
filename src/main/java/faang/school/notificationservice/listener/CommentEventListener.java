package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.CommentEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.CommentMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CommentEventListener extends AbstractEventListener<CommentEvent> implements MessageListener {

    private final UserService userService;

    public CommentEventListener(ObjectMapper objectMapper,
                                CommentMessageBuilder commentMessageBuilder,
                                List<NotificationService> notificationServices,
                                UserService userService) {
        super(objectMapper, notificationServices, commentMessageBuilder);
        this.userService = userService;
    }

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        CommentEvent event = handleEvent(CommentEvent.class, message);
        UserDto commentAuthor = userService.getUser(event.getCommentAuthorId());
        UserDto postAuthor = userService.getUser(event.getPostAuthorId());
        handleNotification(postAuthor, commentAuthor);
    }
}
