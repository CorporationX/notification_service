package faang.school.notificationservice.listener;


import faang.school.notificationservice.event.kafka.GoalCompletionNotificationEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;


@Slf4j
@Component
public class GoalCompletionEventListener extends AbstractEventListener<GoalCompletionNotificationEvent> {

    public GoalCompletionEventListener(
            List<NotificationService> notificationList,
            MessageBuilder<GoalCompletionNotificationEvent> messageBuilder
    ) {
        super(notificationList, messageBuilder);
    }

    @KafkaListener(
            topics = "${spring.kafka.topics.goal-completed-topic.name}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaGoalCompletedEventListener"
    )
    public void listenGoalCompletion(GoalCompletionNotificationEvent event) {
        sendNotification(event);
    }

    @Override
    public boolean isEventValid(GoalCompletionNotificationEvent event) {
        return Objects.nonNull(event) && isUserDtoValid(event.getOwner());
    }
}