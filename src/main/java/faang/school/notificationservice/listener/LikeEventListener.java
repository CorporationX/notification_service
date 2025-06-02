package faang.school.notificationservice.listener;

import faang.school.notificationservice.dto.event.LikeEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Slf4j
@Component
public class LikeEventListener extends AbstractListener<LikeEvent> {

    public LikeEventListener() {
        super(LikeEvent.class);
    }

    @KafkaListener(topics = "${spring.kafka.consumer.topics.LIKED_POST}", groupId = "${spring.kafka.consumer.groupId}")
    public void handle(LikeEvent event) {
        log.info("Like event received: {}", event);
        sendNotification(event.getAuthorId(), getMessage(event, Locale.ENGLISH));
    }
}
