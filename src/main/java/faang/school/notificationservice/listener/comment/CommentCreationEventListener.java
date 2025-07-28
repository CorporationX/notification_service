package faang.school.notificationservice.listener.comment;

import faang.school.notificationservice.event.kafka.CommentCreationNotificationEvent;
import faang.school.notificationservice.listener.DirectNotificationEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationSenderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CommentCreationEventListener extends DirectNotificationEventListener<CommentCreationNotificationEvent> {

    public CommentCreationEventListener(
            NotificationSenderService notificationSender,
            MessageBuilder<CommentCreationNotificationEvent> messageBuilder
    ) {
        super(notificationSender, messageBuilder);
        log.info("CommentCreationEventListener constructor notificationServices:{}", notificationSender);
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
