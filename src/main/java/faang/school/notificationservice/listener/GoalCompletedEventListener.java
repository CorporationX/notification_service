package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.GoalCompletedEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class GoalCompletedEventListener extends AbstractEventListener<GoalCompletedEvent> {

    public GoalCompletedEventListener(UserServiceClient userServiceClient,
                                      List<MessageBuilder<GoalCompletedEvent>> messageBuilders,
                                      List<NotificationService> notificationServices) {
        super(userServiceClient, messageBuilders, notificationServices);
    }

    @KafkaListener(
            topics = "${spring.kafka.topics.goal-completed}",
            properties = "spring.json.value.default.type=faang.school.notificationservice.dto.GoalCompletedEvent"
    )
    @Override
    public void onMessage(GoalCompletedEvent event) {
        handleMessage(event, event.userId());
        log.info("Processing message completed: {}", event);
    }
}
