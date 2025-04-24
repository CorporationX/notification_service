package faang.school.notificationservice.handler;

import faang.school.notificationservice.dto.event.AchievementEventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class AchievementEventHandler {

    @KafkaListener(topics = "writer_achieved_topic", groupId = "not-service")
    public void handel(AchievementEventDto message) {
        log.info("start handle achievement event {}", message);
    }
}
