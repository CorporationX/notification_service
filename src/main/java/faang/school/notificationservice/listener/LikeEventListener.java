package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.PostDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.LikeEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.PostService;
import faang.school.notificationservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class LikeEventListener extends AbstractEventListener<LikeEvent>
        implements MessageListener {
    private final PostService postService;

    public LikeEventListener(ObjectMapper objectMapper, List<NotificationService> notificationServices, MessageBuilder<LikeEvent> messageBuilder, UserService userService, PostService postService) {
        super(objectMapper, notificationServices, messageBuilder, userService);
        this.postService = postService;
    }

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        LikeEvent event = handleEvent(LikeEvent.class, message);
        UserDto author = userService.getUser(event.getAuthorId());
        UserDto likeAuthor = userService.getUser(event.getLikeAuthorId());
        PostDto post = postService.getPostById(event.getPostId());
        Object[] additionData = new Object[]{author.getUsername(), likeAuthor.getUsername(), post.getContent()};
        handleNotification(author, likeAuthor, additionData);
    }
}
