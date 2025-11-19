
package faang.school.notificationservice.listener;

import faang.school.notificationservice.dto.RecommendationReceivedEventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.message_builder.RecommendationReceivedEventMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RecommendationReceivedEventListener {
    private final UserService userService;
    private final RecommendationReceivedEventMessageBuilder recommendationReceivedEventMessageBuilder;
    private final Map<UserDto.PreferredContact, NotificationService> notificationServiceMap;

    public RecommendationReceivedEventListener(UserService userService,
                                               RecommendationReceivedEventMessageBuilder
                                                       recommendationReceivedEventMessageBuilder,
                                               List<NotificationService> notificationServices) {
        this.userService = userService;
        this.recommendationReceivedEventMessageBuilder = recommendationReceivedEventMessageBuilder;
        this.notificationServiceMap = notificationServices.stream()
                .collect(Collectors.toMap(
                                NotificationService::getPreferredContact,
                                Function.identity()
                        )
                );
    }

    @KafkaListener(topics = "${kafka.topic.recommendation-received-events}")
    public void handleRecommendationReceivedEvent(RecommendationReceivedEventDto jsonEvent) {
        try {
            log.info("Successfully listen event from a recommendation-received-events topic: {}", jsonEvent);
            UserDto recommendationReceiver = userService.getUser(jsonEvent.receiverId());
            String message = recommendationReceivedEventMessageBuilder.buildMessage(jsonEvent,
                    recommendationReceiver.getLocale());

            NotificationService service = notificationServiceMap.get(recommendationReceiver.getPreference());
            if (service != null) {
                service.send(recommendationReceiver, message);
            } else {
                log.warn("Not found preference notification service: {}", recommendationReceiver.getPreference());
            }
        } catch (Exception e) {
            log.error("Failed to process recommendation received event {}", jsonEvent, e);
        }
    }
}
