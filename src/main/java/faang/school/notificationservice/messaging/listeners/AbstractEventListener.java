package faang.school.notificationservice.messaging.listeners;

import faang.school.notificationservice.processor.EventProcessor;
import faang.school.notificationservice.service.EventDeserializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.Acknowledgment;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {

    private final EventDeserializer eventDeserializer;
    private final EventProcessor<T> eventProcessor;

    public void handleEvent(String eventJson, Acknowledgment acknowledgment) {
        try {
            log.info("Received event from Kafka");

            T event = eventDeserializer.deserialize(eventJson, eventProcessor.getEventType());
            eventProcessor.process(event);

            acknowledgment.acknowledge();
            log.info("Event processed and acknowledged");

        } catch (Exception e) {
            log.error("Error processing event, will retry", e);
            throw e;
        }
    }
}