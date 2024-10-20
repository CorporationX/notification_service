package faang.school.notificationservice.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.AchievementEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AchievementEventListener implements EventListener<String> {
    private final ObjectMapper mapper;

    @Value("${spring.kafka.topics.achievement-received.name}")
    private String topic;

    @Override
    @KafkaListener(topics = "${spring.kafka.topics.achievement-received.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void onMessage(String message) {
        AchievementEvent event = null;
        try {
             event = mapper.readValue(message, AchievementEvent.class);
        } catch (JsonProcessingException e) {
            log.error("error converting json to obj", e);
        }
        log.info("received: {}", event);
    }
}
