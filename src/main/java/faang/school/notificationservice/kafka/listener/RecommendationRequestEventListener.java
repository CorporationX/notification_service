package faang.school.notificationservice.kafka.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.recommendation.RecommendationRequestEvent;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.recommendation.RecommendationRequestNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecommendationRequestEventListener {

    private final RecommendationRequestNotificationService notificationService;
    private final UserServiceClient userServiceClient;
    private final MessageBuilder<RecommendationRequestEvent> messageBuilder;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${kafka.recommendation.request.topic}", groupId = "notifications-group")
    public void listen(String input) {
        RecommendationRequestEvent recommendationRequestEvent = mapInputToRecommendationRequestEvent(input);
        log.info("Received recommendation request: {}", recommendationRequestEvent);
        UserDto receiverUser = userServiceClient.getUser(recommendationRequestEvent.getReceiverId());
        String message = messageBuilder.buildMessage(recommendationRequestEvent, Locale.getDefault());
        notificationService.send(receiverUser, message);
    }

    private RecommendationRequestEvent mapInputToRecommendationRequestEvent(String input) {
        try {
            return objectMapper.readValue(input, RecommendationRequestEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
