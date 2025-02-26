package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.CommentEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class CommentEventListener extends AbstractEventListener<CommentEvent> {

    public CommentEventListener(UserServiceClient userServiceClient,
                                List<MessageBuilder<CommentEvent>> messageBuilders,
                                List<NotificationService> notificationServices) {
        super(userServiceClient, messageBuilders, notificationServices);
    }

    @KafkaListener(
            topics = "${spring.kafka.topics.comment_create_topic}",
            properties = "spring.json.value.default.type=faang.school.notificationservice.dto.CommentEvent"
    )
    @Override
    public void onMessage(CommentEvent event, Acknowledgment acknowledgment) {
        //TODO: Добавить Locale в сущность пользователя, чтобы ее можно было получать
        String message = getMessage(event, Locale.UK);
        sendNotification(event.getAuthorId(), message);

        acknowledgment.acknowledge();
        log.info("Processing message completed: {}", message);
    }
}
