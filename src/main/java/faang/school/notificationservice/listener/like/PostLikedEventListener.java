package faang.school.notificationservice.listener.like;

import faang.school.notificationservice.event.kafka.PostLikedNotificationEvent;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class PostLikedEventListener extends AbstractEventListener<PostLikedNotificationEvent> {

    public PostLikedEventListener(
            List<NotificationService> notificationList,
            MessageBuilder<PostLikedNotificationEvent> messageBuilder
    ) {
        super(notificationList, messageBuilder);
    }

    @KafkaListener(
            topics = "${spring.kafka.topics.like.post-liked-topic.name}",
            groupId = "${spring.kafka.consumer.post-service.group-id}",
            containerFactory = "kafkaPostLikedEventListener"
    )
    public void listenPostLiked(PostLikedNotificationEvent event) {
        sendNotification(event);
    }

    @Override
    public boolean isEventValid(PostLikedNotificationEvent event) {
        return Objects.nonNull(event) && isUserDtoValid(event.getOwner());
    }
}
