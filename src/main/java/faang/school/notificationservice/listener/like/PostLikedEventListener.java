package faang.school.notificationservice.listener.like;

import faang.school.notificationservice.event.kafka.PostLikedNotificationEvent;
import faang.school.notificationservice.listener.BatchEventListener;
import faang.school.notificationservice.service.notification.handler.PostLikedNotificationEventHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class PostLikedEventListener extends BatchEventListener<PostLikedNotificationEvent> {

    public PostLikedEventListener(PostLikedNotificationEventHandler eventHandler) {
        super(eventHandler);
    }

    @KafkaListener(
            topics = "${spring.kafka.topics.post-liked-topic.name}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaPostLikedEventListener"
    )
    public void listenPostLikedBatch(List<PostLikedNotificationEvent> events) {
        eventHandler.saveNotifications(events);
    }

    @Override
    public boolean isEventValid(PostLikedNotificationEvent event) {
        return Objects.nonNull(event) && isUserDtoValid(event.owner());
    }
}
