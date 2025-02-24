package faang.school.notificationservice.kafka.listener.recommendation;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.recommendation.RecommendationRequestEvent;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.recommendation.RecommendationRequestEventService;
import faang.school.notificationservice.utils.EventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.EventListener;
import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecommendationRequestEventListener implements EventListener {

    private final RecommendationRequestEventService notificationService;
    private final UserServiceClient userServiceClient;
    private final MessageBuilder<RecommendationRequestEvent> messageBuilder;
    private final EventMapper<RecommendationRequestEvent> eventMapper;

    @KafkaListener(topics = "${kafka.recommendation.request.topic}", groupId = "notifications-group")
    public void listen(String input) {
        RecommendationRequestEvent recommendationRequestEvent =
                eventMapper.mapMessageToEvent(input, RecommendationRequestEvent.class);
        log.info("Received recommendation request: {}", recommendationRequestEvent);
        UserDto receiverUser = userServiceClient.getUser(recommendationRequestEvent.getReceiverId());
        String message = messageBuilder.buildMessage(recommendationRequestEvent, Locale.getDefault());
        notificationService.apply(receiverUser, message);
    }
}
