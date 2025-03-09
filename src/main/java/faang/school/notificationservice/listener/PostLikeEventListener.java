package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.LikePostEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class PostLikeEventListener extends  AbstractEventListener<LikePostEvent> {
    public PostLikeEventListener(UserServiceClient userServiceClient,
                                 List<MessageBuilder<LikePostEvent>> messageBuilders,
                                 List<NotificationService> notificationServices) {
        super(userServiceClient, messageBuilders, notificationServices);
    }

    @KafkaListener(
            topics = "${spring.kafka.topics.post-like-topic}",
            properties = "spring.json.value.default.type=faang.school.notificationservice.dto.LikePostEvent"
    )
    @Override
    public void onMessage(LikePostEvent event) {
        handleMessage(event, event.authorId());
        log.info("Processing message completed: {}", event);
    }
}
