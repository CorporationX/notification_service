package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserNotificationDto;
import faang.school.notificationservice.listener.event.CommentEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CommentEventListener extends AbstractEventListener<CommentEvent> {

    @Autowired
    public CommentEventListener(UserServiceClient userServiceClient,
                                Map<UserNotificationDto.PreferredContact, NotificationService> notificationServicesMap,
                                Map<Class<?>, MessageBuilder<?>> messageBuildersMap) {
        super(userServiceClient, notificationServicesMap, messageBuildersMap);
    }

    @KafkaListener(topics = "${spring.kafka.consumer.comment-create.topic}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory")
    public void commentEventListner(CommentEvent commentEvent) {
        UserNotificationDto userAuthorPost = userServiceClient.getUserNotificationDto(commentEvent.getAuthorPostId());
        String message = getMessage(commentEvent, userAuthorPost.getLocale());
        sendNotification(userAuthorPost.getId(), message);
    }
}