package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.CommentEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class CommentEventListener extends AbstractEventListener<CommentEvent> {

    public CommentEventListener(List<MessageBuilder<CommentEvent>> messageBuilders,
                                List<NotificationService> notificationServices, UserServiceClient userServiceClient) {
        super(userServiceClient, messageBuilders, notificationServices);
    }

    @KafkaListener(
            topics = "${spring.kafka.topics.comment_create_topic}",
            properties = "spring.json.value.default.type=faang.school.notificationservice.dto.CommentEvent"
    )
    @Override
    public void onMessage(CommentEvent event) {
        handleMessage(event, event.getAuthorId());
        log.info("Processing message completed: {}", event);
    }
}
