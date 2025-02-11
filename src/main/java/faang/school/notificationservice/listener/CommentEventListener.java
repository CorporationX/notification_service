package faang.school.notificationservice.listener;

import faang.school.notificationservice.dto.CommentEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CommentEventListener {

    @KafkaListener(topics = "comment-events")
    public void commentEventListen(CommentEvent comment) {
        log.info("Received comment event: {}", comment);
    }
}
