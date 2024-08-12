package faang.school.notificationservice.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.MessageDto;
import faang.school.notificationservice.dto.PostDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.FollowerEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.PostService;
import faang.school.notificationservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Service
@Slf4j
public class FollowerEventSubscriber extends RedisAbstractMessageSubscriber<FollowerEvent>
        implements MessageListener {

    public FollowerEventSubscriber(ObjectMapper objectMapper,
                                   UserService userService,
                                   PostService postService,
                                   List<NotificationService> notificationServices,
                                   List<MessageBuilder<FollowerEvent>> messageBuilders) {
        super(objectMapper, userService, postService, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        try {
            FollowerEvent event = objectMapper.readValue(message.getBody(), FollowerEvent.class);
            UserDto author = userService.getUser(event.getAuthorId());
            UserDto likeAuthor = userService.getUser(event.getLikeAuthorId());
            PostDto post = postService.getPostById(event.getPostId());
            MessageDto messageDto = MessageDto.builder()
                    .authorName(author.getUsername())
                    .likeAuthorName(likeAuthor.getUsername())
                    .postName(post.getContent())
                    .build();
            sendNotification(author.getId(), getMessage(event, messageDto, Locale.UK));
        } catch (IOException e) {
            log.error("Failed to process message: {}. Exception: {}", new String(message.getBody()), e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
