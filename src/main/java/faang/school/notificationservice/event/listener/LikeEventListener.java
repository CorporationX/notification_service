package faang.school.notificationservice.event.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.LikeEvent;
import faang.school.notificationservice.service.NotificationServiceHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LikeEventListener extends AbstractEventListener<LikeEvent> {

    private final NotificationServiceHandler notificationServiceHandler;

    public LikeEventListener(
            ObjectMapper objectMapper,
            UserServiceClient userServiceClient,
            NotificationServiceHandler notificationServiceHandler) {
        super(objectMapper, userServiceClient);
        this.notificationServiceHandler = notificationServiceHandler;
    }

    @KafkaListener(topics = "${spring.kafka.topics.like-events}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleLikeEvent(String eventJson) {
        LikeEvent event = parseEvent(eventJson, LikeEvent.class);
        log.info("Received LikeEvent: {}", event);

        UserDto user = getUser(event.getAuthorId());
        String message = "User " + event.getUserId() + " liked your post " + event.getPostId();

        notificationServiceHandler.sendNotification(user, message);
    }
}