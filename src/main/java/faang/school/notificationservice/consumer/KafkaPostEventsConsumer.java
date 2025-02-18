package faang.school.notificationservice.consumer;

import faang.school.notificationservice.builder.LikeEventBuilder;
import faang.school.notificationservice.dto.LikeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaPostEventsConsumer {
    private final LikeEventBuilder likeEventBuilder;

    @KafkaListener(topics = "${spring.kafka.like_post_event_topic_name}", groupId = "group-id")
    public void onLikePostEvent(String message) {
        System.out.println("LIKE EVENT DETECTED: " + message);
        LikeEvent event = likeEventBuilder.build(message);
        /// ...
        /// ...
    }
}
