package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.PostLikeEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Slf4j
@Service
public class PostLikeEventListener extends AbstractEventListener {

    public PostLikeEventListener(ObjectMapper objectMapper,
                                 UserServiceClient userServiceClient,
                                 List<NotificationService> notificationServices,
                                 List<MessageBuilder<PostLikeEvent>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @KafkaListener(topics = "${spring.kafka.consumer.topics.notification.post-like-topic}",
            groupId = "${spring.kafka.consumer.groups.notification.post-like-group-id}")
    public void listen(String message) {
        handleEvent(message, PostLikeEvent.class, event -> {
            PostLikeEvent likeEvent = (PostLikeEvent) event;
            UserDto author = userServiceClient.getUser(likeEvent.getPostAuthorId());
            String text = getMessage(likeEvent, Locale.UK);
            sendNotification(author, text);
            log.info("Successfully parsed event postId: {}, postAuthorId: {}, likeAuthorId: {}",
                    likeEvent.getPostId(), likeEvent.getPostAuthorId(), likeEvent.getLikeAuthorId());
        });
    }
}
