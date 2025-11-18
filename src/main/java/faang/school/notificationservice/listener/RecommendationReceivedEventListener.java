
package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.RecommendationReceivedEventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.message_builder.RecommendationReceivedEventMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.user.UserService;
import jakarta.annotation.PostConstruct;
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
    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final RecommendationReceivedEventMessageBuilder recommendationReceivedEventMessageBuilder;
    private final Map<UserDto.PreferredContact, NotificationService> notificationServiceMap;

    public RecommendationReceivedEventListener(ObjectMapper objectMapper,
                                               UserService userService,
                                               RecommendationReceivedEventMessageBuilder
                                                       recommendationReceivedEventMessageBuilder,
                                               List<NotificationService> notificationServices) {
        this.objectMapper = objectMapper;
        this.userService = userService;
        this.recommendationReceivedEventMessageBuilder = recommendationReceivedEventMessageBuilder;
        this.notificationServiceMap = notificationServices.stream()
                .collect(Collectors.toMap(
                                NotificationService::getPreferredContact,
                                Function.identity()
                        )
                );
    }

    @PostConstruct
    public void init() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @KafkaListener(topics = "${app.kafka.topics.recommendation-received-events}")
    public void handleRecommendationReceivedEvent(String jsonEvent) {
        try {
            RecommendationReceivedEventDto recommendationReceivedEventDto =
                    objectMapper.readValue(jsonEvent, RecommendationReceivedEventDto.class);
            log.info("Successfully listen event from a recommendation-received-events topic");
            UserDto recommendationReceiver = userService.getUser(recommendationReceivedEventDto.receiverId());
            String message = recommendationReceivedEventMessageBuilder.buildMessage(recommendationReceivedEventDto,
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
