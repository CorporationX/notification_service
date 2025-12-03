package faang.school.notificationservice.messaging.comment;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.CommentEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommentEventListener {

    private final UserServiceClient userServiceClient;
    private final List<MessageBuilder<?>> messageBuilders;
    private final List<NotificationService> notificationServices;

    @KafkaListener(
            topics = "${spring.kafka.topic.comment-events}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "commentEventKafkaListenerContainerFactory"
    )
    public void onCommentEvent(CommentEvent event) {
        log.info("Received comment event: commentId={}, postId={}, postAuthorId={}",
                event.getCommentId(), event.getPostId(), event.getPostAuthorId());

        try {
            UserDto postAuthor = userServiceClient.getUser(event.getPostAuthorId());
            if (postAuthor == null) {
                log.warn("Cannot send notification: user with id {} not found", event.getPostAuthorId());
                return;
            }

            MessageBuilder<CommentEvent> messageBuilder = findMessageBuilder(event);
            if (messageBuilder == null) {
                log.error("No MessageBuilder found for event class {}", event.getClass().getSimpleName());
                return;
            }

            Locale locale = postAuthor.getLocale() != null 
                    ? postAuthor.getLocale() 
                    : Locale.getDefault();
            String message = messageBuilder.buildMessage(event, locale);
            sendNotification(postAuthor, message);
            log.info("Notification sent successfully to user {} via {}", postAuthor.getId(), postAuthor.getPreference());
        } catch (Exception e) {
            log.error("Error processing comment event: commentId={}, postId={}, error={}", event.getCommentId(), event.getPostId(), e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> MessageBuilder<T> findMessageBuilder(T event) {
        return (MessageBuilder<T>) messageBuilders.stream()
                .filter(builder ->
                        builder.getInstance().isInstance(event))
                .findFirst()
                .orElse(null);
    }

    private void sendNotification(UserDto user, String message) {
        NotificationService notificationService = notificationServices.stream().filter(service ->
                service.getPreferredContact()
                        .equals(user.getPreference()))
                .findFirst()
                .orElse(null);

        if (notificationService == null) {
            log.warn("No NotificationService found for preference {}. Skipping notification.", user.getPreference());
            return;
        }
        notificationService.send(user, message);
    }


}
