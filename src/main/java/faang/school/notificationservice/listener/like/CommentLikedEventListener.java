package faang.school.notificationservice.listener.like;

import faang.school.notificationservice.event.kafka.CommentLikedNotificationEvent;
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
public class CommentLikedEventListener extends AbstractEventListener<CommentLikedNotificationEvent> {

    public CommentLikedEventListener(
            List<NotificationService> notificationList,
            MessageBuilder<CommentLikedNotificationEvent> messageBuilder
    ) {
        super(notificationList, messageBuilder);
    }

    @KafkaListener(
            topics = "${spring.kafka.topics.like.comment-liked-topic.name}",
            groupId = "${spring.kafka.consumer.post-service.group-id}",
            containerFactory = "kafkaCommentLikedEventListener"
    )
    public void listenCommentLiked(CommentLikedNotificationEvent event) {
        sendNotification(event);
    }

    @Override
    public boolean isEventValid(CommentLikedNotificationEvent event) {
        return Objects.nonNull(event) && isUserDtoValid(event.getOwner());
    }
}
