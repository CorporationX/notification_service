package faang.school.notificationservice.kafka.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.recommendation.RecommendationRequestEvent;
import faang.school.notificationservice.service.recommendation.RecommendationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecommendationRequestEventListener {

    private final RecommendationService recommendationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${kafka.recommendation.request.topic}", groupId = "notifications-group")
    public void listen(String message) {
        RecommendationRequestEvent recommendationRequestEvent = mapInputToRecommendationRequestEvent(message);
        log.info("Received recommendation request: {}", recommendationRequestEvent);
        recommendationService.sendNotification(recommendationRequestEvent);
    }

    private RecommendationRequestEvent mapInputToRecommendationRequestEvent(String input) {
        try {
            return objectMapper.readValue(input, RecommendationRequestEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
