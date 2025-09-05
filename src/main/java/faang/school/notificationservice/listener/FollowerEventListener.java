package faang.school.notificationservice.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class FollowerEventListener extends AbstractEventListener<FollowerEvent> implements MessageListener<String, String> {

    public FollowerEventListener(ObjectMapper objectMapper,
                                 UserServiceClient userServiceClient,
                                 List<NotificationService> notificationServices,
                                 List<MessageBuilder<FollowerEvent>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    @KafkaListener(topics = "${kafka.topics.follower}", groupId = "follower-event-group", containerFactory = "kafkaListenerContainerFactory")
    public void onMessage(ConsumerRecord<String, String> record) {
        try {
            FollowerEvent event = objectMapper.readValue(record.value(), FollowerEvent.class);
            String notificationText = getMessage(event, Locale.UK);
            sendNotification(event.getFolloweeId(), notificationText);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка при десериализации сообщения", e);
        }
    }
}
