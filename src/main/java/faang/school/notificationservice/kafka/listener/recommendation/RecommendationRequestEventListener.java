package faang.school.notificationservice.kafka.listener.recommendation;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.recommendation.RecommendationRequestEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.recommendation.RecommendationRequestEventService;
import faang.school.notificationservice.service.recommendation.RecommendationService;
import faang.school.notificationservice.utils.EventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.EventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecommendationRequestEventListener implements EventListener {

    private final RecommendationRequestEventService notificationService;
    private final UserServiceClient userServiceClient;
    private final MessageBuilder<RecommendationRequestEvent> messageBuilder;
    private final EventMapper<RecommendationRequestEvent> eventMapper;
    private final RecommendationService recommendationService;

    @KafkaListener(topics = "${kafka.recommendation.request.topic}", groupId = "notifications-group")
    public void listen(String message) {
        RecommendationRequestEvent recommendationRequestEvent =
                eventMapper.mapMessageToEvent(message, RecommendationRequestEvent.class);
        log.info("Received recommendation request: {}", recommendationRequestEvent);
        recommendationService.sendNotification(recommendationRequestEvent)
    }
}
