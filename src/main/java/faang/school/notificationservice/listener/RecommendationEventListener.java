package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.RecommendationRequestedEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class RecommendationEventListener
        extends AbstractEventListener<RecommendationRequestedEvent>
        implements MessageListener {

    public RecommendationEventListener(ObjectMapper objectMapper,
                                       UserServiceClient userServiceClient,
                                       List<MessageBuilder<RecommendationRequestedEvent>> messageBuilders,
                                       List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @KafkaListener(topics = "recommendation_request", groupId = "notification-processing-group")
    @Override
    public void onMessage(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        handleEvent(record, RecommendationRequestedEvent.class, event -> {
            String message = getMessage(event, Locale.getDefault());
            sendNotification(event.targetUserId(), message);
        });
        acknowledgment.acknowledge();
    }
}
