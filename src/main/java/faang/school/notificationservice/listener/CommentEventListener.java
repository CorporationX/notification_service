package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserEventDto;
import faang.school.notificationservice.listener.event.CommentEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommentEventListener extends AbstractEventListener<CommentEvent> {

    @Autowired
    public CommentEventListener(List<MessageBuilder<CommentEvent>> messageBuilders,
                                List<NotificationService> notificationServiceList,
                                UserServiceClient userServiceClient) {
        super(messageBuilders, notificationServiceList, userServiceClient);
    }

    @KafkaListener(topics = "${user.comment.kafka_topic}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory")
    public void commentEventListner(CommentEvent commentEvent) {
        UserEventDto userAuthorPost = userServiceClient.getUserForEvent(commentEvent.getAuthorPostId());
        String message = getMessage(commentEvent, userAuthorPost.getLocale());
        sendNotification(userAuthorPost.getId(), message);
    }
}