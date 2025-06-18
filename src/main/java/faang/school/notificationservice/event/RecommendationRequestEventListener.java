package faang.school.notificationservice.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.FeignUserServiceAdapter;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class RecommendationRequestEventListener extends AbstractEventListener<RecommendationRequestEvent> {
    public RecommendationRequestEventListener(ObjectMapper objectMapper,
                                              List<MessageBuilder<? extends Event>> messageBuilders,
                                              List<NotificationService> notificationServices,
                                              FeignUserServiceAdapter feignUserServiceAdapter) {
        super(objectMapper, messageBuilders, notificationServices, feignUserServiceAdapter);
    }

    @KafkaListener(
            topics = "${spring.kafka.topics.recommendation-request.name}",
            concurrency = "${spring.kafka.topics.recommendation-request.concurrency}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(ConsumerRecord<String, String> message, Acknowledgment ack) {
        handleEvent(message, RecommendationRequestEvent.class, event -> {
            UserDto receiver = event.getReceiver();
            sendNotification(receiver, getMessage(event, receiver.getLocale()));
            ack.acknowledge();
        });
    }
}
