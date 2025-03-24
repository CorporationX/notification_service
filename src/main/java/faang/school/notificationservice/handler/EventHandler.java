package faang.school.notificationservice.handler;

import faang.school.notificationservice.entity.Event;
import faang.school.notificationservice.exception.impl.non_retryable.DuplicateEventException;
import faang.school.notificationservice.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class EventHandler {
    private String kafkaEventKey;
    private final EventService eventService;

    public void checkEventDuplicatedThrow(ConsumerRecord<String, Object> kafkaEvent) {
        String key = kafkaEvent.key();
        if (key == null) {
            log.warn("Received Kafka event without a key. Skipping duplicate check.");
            return;
        }

        if (eventService.existsById(key)) {
            String error = "Duplicate event received: " + key;
            log.warn(error);
            throw new DuplicateEventException(error);
        }
        kafkaEventKey = key;
    }

    public void saveEvent() {
        eventService.save(new Event(kafkaEventKey));
    }
}
