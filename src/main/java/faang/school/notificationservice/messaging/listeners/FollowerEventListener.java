package faang.school.notificationservice.messaging.listeners;

import faang.school.notificationservice.dto.FollowerEventDto;
import faang.school.notificationservice.processor.EventProcessor;
import faang.school.notificationservice.service.EventDeserializer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class FollowerEventListener extends AbstractEventListener<FollowerEventDto> {

    public FollowerEventListener(
            EventDeserializer eventDeserializer,
            EventProcessor<FollowerEventDto> eventProcessor) {
        super(eventDeserializer, eventProcessor);
    }

    @KafkaListener(
            topics = "${kafka.topics.follower}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(String eventJson, Acknowledgment acknowledgment) {
        handleEvent(eventJson, acknowledgment);
    }
}