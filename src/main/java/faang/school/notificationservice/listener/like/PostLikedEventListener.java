package faang.school.notificationservice.listener.like;

import faang.school.notificationservice.event.kafka.PostLikedNotificationEvent;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.service.notification.handler.PostLikedNotificationEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostLikedEventListener extends AbstractEventListener<PostLikedNotificationEvent> {

    private final PostLikedNotificationEventHandler eventHandler;

    @KafkaListener(
            topics = "${spring.kafka.topics.post-liked-topic.name}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaPostLikedEventListener"
    )
    public void listenPostLiked(PostLikedNotificationEvent event) {
        if (isEventValid(event)) {
            eventHandler.saveNotification(event);
        } else {
            log.error("Event validation failed. Event: {}", event);
        }
    }

    @Override
    public boolean isEventValid(PostLikedNotificationEvent event) {
        return Objects.nonNull(event) && isUserDtoValid(event.getOwner());
    }
}
