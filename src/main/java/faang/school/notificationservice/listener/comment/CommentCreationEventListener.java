package faang.school.notificationservice.listener.comment;

import faang.school.notificationservice.event.kafka.CommentCreationNotificationEvent;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class CommentCreationEventListener extends AbstractEventListener<CommentCreationNotificationEvent> {

    public CommentCreationEventListener(
            List<NotificationService> notificationServices,
            MessageBuilder<CommentCreationNotificationEvent> messageBuilder
    ) {
        super(notificationServices, messageBuilder);
        log.info("CommentCreationEventListener constructor notificationServices:{}", notificationServices);
    }

    @KafkaListener(
            topics = "${spring.kafka.topics.comment-created-topic.name}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaCommentCreationEventListener"
    )
    public void listenCommentCreation(CommentCreationNotificationEvent event) {
        log.info("listenCommentCreation {}", event);
        sendNotification(event);
    }

    @Override
    public boolean isEventValid(CommentCreationNotificationEvent event) {
        return validateObjectNonNullData(event, event::getOwner, event::getShortContent, event::getCommentAuthorUserName)
                && isUserDtoValid(event.getOwner());
    }
}
