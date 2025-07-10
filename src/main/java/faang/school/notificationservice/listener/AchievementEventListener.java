package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.AchievementEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class AchievementEventListener extends AbstractEventListener<AchievementEvent> {

    public AchievementEventListener(ObjectMapper objectMapper,
                                    List<MessageBuilder<AchievementEvent>> messageBuilders,
                                    UserServiceClient userServiceClient,
                                    List<NotificationService> notificationServices) {
        super(objectMapper, messageBuilders, userServiceClient, notificationServices);
    }

    @KafkaListener(
            topics = "${spring.kafka.topics.achievement}",
            groupId = "notification-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void onMessage(@Payload AchievementEvent event,
                          @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        try {
            log.info("Received achievement event with key {}: {}", key, event);
        } catch (Exception e) {
            log.error("Error processing achievement event: {}", event, e);
        }
    }
}