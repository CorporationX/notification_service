package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.GoalCompletedEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class GoalCompletedEventListener extends AbstractEventListener<GoalCompletedEvent> implements MessageListener {

    public GoalCompletedEventListener(ObjectMapper objectMapper,
                                      UserServiceClient userServiceClient,
                                      List<MessageBuilder<GoalCompletedEvent>> messageBuilders,
                                      List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @KafkaListener(topics = "goal_completed")
    @Override
    public void onMessage(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        handleEvent(record, GoalCompletedEvent.class, event -> {
            String message = getMessage(event, Locale.UK);
            sendNotification(event.userId(), message);
        });
        acknowledgment.acknowledge();
    }
}
