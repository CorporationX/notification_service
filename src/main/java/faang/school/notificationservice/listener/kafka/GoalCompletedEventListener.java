package faang.school.notificationservice.listener.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.GoalCompletedEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class GoalCompletedEventListener extends AbstractMessageProcessor<GoalCompletedEvent> {
    private final UserServiceClient userServiceClient;
    private final ObjectMapper objectMapper;
    private final Locale absolutelyCustomLocale = Locale.ENGLISH;

    @Value("${spring.data.kafka.use-kafka}")
    private boolean useKafka;

    public GoalCompletedEventListener(List<NotificationService> notificationServices,
                                      List<MessageBuilder<GoalCompletedEvent>> messageBuilders,
                                      UserServiceClient userServiceClient,
                                      ObjectMapper objectMapper) {
        super(messageBuilders, notificationServices);
        this.userServiceClient = userServiceClient;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "goal_complete")
    public void listen(String json) {
        if (!useKafka) return;
        try {
            GoalCompletedEvent event = objectMapper.readValue(json, GoalCompletedEvent.class);
            String generalizedNotification = getMessage(absolutelyCustomLocale, event);
            List<UserDto> usersCompletedGoal = userServiceClient.getUsersByIds(event.userIds());

            usersCompletedGoal.forEach(userDto -> {
                String personalNotification = generalizedNotification.formatted(userDto.getUsername());
                sendNotification(userDto, personalNotification);
            });

            log.debug("Notification(s) about goal {} completion were sent to users with ids: {}",
                    event.goalTitle(), event.userIds());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
