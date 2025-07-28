package faang.school.notificationservice.listener.like;

import faang.school.notificationservice.event.kafka.CommentLikedNotificationEvent;
import faang.school.notificationservice.listener.BatchEventListener;
import faang.school.notificationservice.service.notification.handler.CommentLikedNotificationEventHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommentLikedEventListener extends BatchEventListener<CommentLikedNotificationEvent> {

    public CommentLikedEventListener(CommentLikedNotificationEventHandler eventHandler) {
        super(eventHandler);
    }

    @KafkaListener(
            topics = "${spring.kafka.topics.comment-liked-topic.name}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaCommentLikedEventListener"
    )
    public void listenCommentLikedBatch(List<CommentLikedNotificationEvent> events) {
        processEventsBatch(events);
    }

    @Override
    public boolean isEventValid(CommentLikedNotificationEvent event) {
        return event != null &&
                event.getOwner() != null &&
                super.isUserDtoValid(event.getOwner());
    }
}
