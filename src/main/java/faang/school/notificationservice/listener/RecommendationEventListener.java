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
public class RecommendationEventListener extends AbstractEventListener<RecommendationRequestedEvent> {

    public RecommendationEventListener(UserServiceClient userServiceClient,
                                       List<MessageBuilder<RecommendationRequestedEvent>> messageBuilders,
                                       List<NotificationService> notificationServices) {
        super(userServiceClient, messageBuilders, notificationServices);
    }

    @KafkaListener(
            topics = "${spring.kafka.topics.recommendation_request}",
            properties = "spring.json.value.default.type=faang.school.notificationservice.dto.RecommendationRequestedEvent"
    )
    @Override
    public void onMessage(RecommendationRequestedEvent event) {
        handleMessage(event, event.targetUserId());
        log.info("Processing recommendation event completed: {}", event);
    }
}
