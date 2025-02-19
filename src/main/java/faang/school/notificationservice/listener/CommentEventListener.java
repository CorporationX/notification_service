package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.SmsSendingException;
import faang.school.notificationservice.listener.event.CommentEvent;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class CommentEventListener {
    private final List<NotificationService> notificationServiceList;
    private final UserServiceClient userServiceClient;
    private final MessageSource messageSource;

    @KafkaListener(topics = "${user.comment.kafka_topic}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory")
    public void commentEventListner(CommentEvent commentEvent) {
        UserDto authorComment = userServiceClient.getUser(commentEvent.getAuthorCommentId());
        UserDto authorPost = userServiceClient.getUser(commentEvent.getAuthorPostId());
        String message = messageSource.getMessage("comment.new", new Object[]{authorComment.getUsername(),
                commentEvent.getPostId()}, Locale.getDefault());

        NotificationService service = notificationServiceList.stream()
                .filter(notify -> notify.getPreferredContact() == authorPost.getPreference())
                .findFirst()
                .orElseThrow(() -> new SmsSendingException("Type lalala"));
        service.send(authorPost, message);


    }
}
