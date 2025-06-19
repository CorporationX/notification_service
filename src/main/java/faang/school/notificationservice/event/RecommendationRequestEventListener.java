package faang.school.notificationservice.event;

import faang.school.notificationservice.client.FeignUserServiceAdapter;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class RecommendationRequestEventListener extends AbstractEventListener<RecommendationRequestEvent> {
    public RecommendationRequestEventListener(List<MessageBuilder<? extends Event>> messageBuilders,
                                              List<NotificationService> notificationServices,
                                              FeignUserServiceAdapter feignUserServiceAdapter) {
        super(messageBuilders, notificationServices, feignUserServiceAdapter);
    }

    @KafkaListener(
            topics = "${spring.kafka.topics.recommendation-request.name}",
            concurrency = "${spring.kafka.topics.recommendation-request.concurrency}",
            containerFactory = "recommendationRequestKafkaListenerContainerFactory"
    )
    public void listen(RecommendationRequestEvent event, Acknowledgment ack) {
        handleEvent(event, recommendationRequestEvent -> {
            UserDto receiver = recommendationRequestEvent.getReceiver();
            sendNotification(receiver, getMessage(recommendationRequestEvent, receiver.getLocale()));
            ack.acknowledge();
        });
    }
}
