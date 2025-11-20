package faang.school.notificationservice.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.EventStartEventDto;
import faang.school.notificationservice.service.EventNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.support.Acknowledgment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventsEventConsumer {

    private final EventNotificationService eventNotificationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "${spring.kafka.topic.events}",
            containerFactory = "eventConcurrentKafkaListenerContainerFactory"
    )
    public void handleEventListener(@Payload Map<String, Object> message, Acknowledgment  ack) {

        EventStartEventDto eventStartEventDto = objectMapper.convertValue(message, EventStartEventDto.class);

        eventNotificationService.processEventNotification(eventStartEventDto);

        ack.acknowledge();
    }
}
