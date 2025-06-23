package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.GoalCompletionNotificationEvent;
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

    public GoalCompletionEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient,
                                       List<NotificationService> notificationList,
                                       List<MessageBuilder<GoalCompletionNotificationEvent>> messageBuilders, SmsService smsService) {
        super(objectMapper, userServiceClient, notificationList, messageBuilders);
        this.smsService = smsService;
    }

    private final SmsService smsService;

    @KafkaListener(topics = "${spring.kafka.topics.goal-completed-topic.name}",
            groupId = "${spring.kafka.consumer.group-id}")
    public void listenGoalCompletion(String event) {
        handleEvent(event, GoalCompletionNotificationEvent.class, goalEvent -> {
            String message = getMessage(goalEvent, Locale.getDefault());
            smsService.sendSms(message);
        });
    }
}
