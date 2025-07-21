package faang.school.notificationservice.listener.like;

import faang.school.notificationservice.event.kafka.CommentLikedNotificationEvent;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.service.notification.handler.CommentLikedNotificationEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentLikedEventListener extends AbstractEventListener<CommentLikedNotificationEvent> {

    private final CommentLikedNotificationEventHandler eventHandler;

    @KafkaListener(
            topics = "${spring.kafka.topics.comment-liked-topic.name}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaCommentLikedEventListener"
    )
    public void listenCommentLiked(CommentLikedNotificationEvent event) {
        if (isEventValid(event)) {
            eventHandler.saveNotification(event);
        } else {
            log.error("Event validation failed. Event: {}", event);
        }
    }

    @Override
    public boolean isEventValid(CommentLikedNotificationEvent event) {
        return Objects.nonNull(event) && isUserDtoValid(event.getOwner());
    }
}
