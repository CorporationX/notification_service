package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.RecommendationRequestedEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RecommendationRequestEventListener extends AbstractEventListener<RecommendationRequestedEvent> {

    public RecommendationRequestEventListener(
            List<MessageBuilder<RecommendationRequestedEvent>> messageBuilders,
            List<NotificationService> notificationServices,
            UserServiceClient userServiceClient) {
        super(userServiceClient, messageBuilders, notificationServices);
    }

    @KafkaListener(
            topics = "${spring.kafka.topics.recommendation_requested}",
            properties = "spring.json.value.default.type=faang.school.notificationservice.dto.RecommendationRequestedEvent"
    )
    @Override
    public void onMessage(RecommendationRequestedEvent event) {
        handleMessage(event, event.targetUserId());
        log.info("Recommendation request processed: {}", event);
    }
}
