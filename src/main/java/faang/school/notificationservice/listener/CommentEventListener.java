package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.CommentEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.CommentMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Slf4j
@Service
public class CommentEventListener extends AbstractEventListener<CommentEvent> implements MessageListener {

    private final CommentMessageBuilder commentMessageBuilder;
    private final List<NotificationService> notificationServices;
    private final UserService userService;

    public CommentEventListener(ObjectMapper objectMapper,
                                CommentMessageBuilder commentMessageBuilder,
                                List<NotificationService> notificationServices, UserService userService) {
        super(objectMapper);
        this.commentMessageBuilder = commentMessageBuilder;
        this.notificationServices = notificationServices;
        this.userService = userService;
    }

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        CommentEvent event = handleEvent(CommentEvent.class, message);
        UserDto commentAuthor = userService.getUser(event.getCommentAuthorId());
        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact()
                        .equals(UserDto.PreferredContact.EMAIL))
                .findFirst()
                .ifPresent(notificationService ->
                        notificationService.send(commentAuthor, commentMessageBuilder
                                .buildMessage(commentAuthor, Locale.getDefault())));
    }
}
