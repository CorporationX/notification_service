package faang.school.notificationservice.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.like.LikeEventDto;
import faang.school.notificationservice.dto.like.UnlikeEventDto;
import faang.school.notificationservice.service.LikeNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class LikeEventConsumer {
    private final LikeNotificationService likeNotificationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${spring.kafka.topics.like-events}",
            containerFactory = "eventConcurrentKafkaListenerContainerFactory")
    public void handleLikeEvent(@Payload Map<String, Object> message,
                                Acknowledgment ack) {
        try {
            LikeEventDto likeEventDto = objectMapper.convertValue(message, LikeEventDto.class);
            likeNotificationService.processLikeEvent(likeEventDto);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error processing like event: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "${spring.kafka.topics.unlike-events}",
            containerFactory = "eventConcurrentKafkaListenerContainerFactory")
    public void handleUnlikeEvent(@Payload Map<String, Object> message,
                                Acknowledgment ack) {
        try {
            UnlikeEventDto unlikeEventDto = objectMapper.convertValue(message, UnlikeEventDto.class);
            likeNotificationService.processUnlikeEvent(unlikeEventDto);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error processing like event: {}", e.getMessage(), e);
        }
    }
}
