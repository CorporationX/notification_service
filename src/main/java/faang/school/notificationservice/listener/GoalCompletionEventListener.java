package faang.school.notificationservice.listener;


import faang.school.notificationservice.event.kafka.GoalCompletionNotificationEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationSenderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Slf4j
@Component
public class GoalCompletionEventListener extends DirectNotificationEventListener<GoalCompletionNotificationEvent> {

    public GoalCompletionEventListener(
            NotificationSenderService notificationSender,
            MessageBuilder<GoalCompletionNotificationEvent> messageBuilder
    ) {
        super(notificationSender, messageBuilder);
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
        return Objects.nonNull(event) && isUserDtoValid(event.owner());
    }
}