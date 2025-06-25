package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.model.dto.event.GoalCompletionNotificationEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;


@Slf4j
@Component
public class GoalCompletionEventListener extends AbstractEventListener<GoalCompletionNotificationEvent> {

    public GoalCompletionEventListener(ObjectMapper objectMapper, List<NotificationService> notificationList,
                                       List<MessageBuilder<GoalCompletionNotificationEvent>> messageBuilders) {
        super(objectMapper, notificationList, messageBuilders);
    }

    @KafkaListener(topics = "${spring.kafka.topics.goal-completed-topic.name}",
            groupId = "${spring.kafka.consumer.group-id}", containerFactory = "kafkaListenerContainerFactory")
    public void listenGoalCompletion(GoalCompletionNotificationEvent event) {
        String message = getMessage(event, Locale.getDefault());
        sendNotification(event.getUserDto(), message);
    }
}
