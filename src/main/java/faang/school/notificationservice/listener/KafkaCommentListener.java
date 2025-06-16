package faang.school.notificationservice.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaCommentListener {
    @KafkaListener(topics = "comment_topic")
    public void listen(String message) {
        log.info("Получено сообщение: " + message);
    }
}
