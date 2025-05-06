package faang.school.notificationservice.service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.PostServiceClient;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.comment.CommentEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.post.PostResponseDto;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.messageBuilder.MessageBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CommentEventListener extends AbstractEventListener<CommentEvent> {
    private final PostServiceClient postServiceClient;

    public CommentEventListener(ObjectMapper objectMapper,
                                UserServiceClient userServiceClient,
                                List<MessageBuilder<CommentEvent>> messageBuilders,
                                List<NotificationService> notificationServiceList,
                                PostServiceClient postServiceClient) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServiceList);
        this.postServiceClient = postServiceClient;
    }


    @KafkaListener(topics = "${spring.kafka.consumer.topics.notification-topic}",
            groupId = "${spring.kafka.consumer.groups.notification-group}")
    public void listen(ConsumerRecord<String, String> record) {
        handleEvent(record, CommentEvent.class, event -> {
            PostResponseDto post = postServiceClient.getPostById(event.getPostId());
            UserDto authorPost = userServiceClient.getUser(post.getAuthorId());
            String text = getMessage(event, authorPost.getLocale());
            sendNotification(authorPost.getId(), text);
        });
    }
}