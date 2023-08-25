package faang.school.notificationservice.publisher;

import faang.school.notificationservice.dto.EventStartEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class EventStartEventPublisher {
//
//    private final RedisTemplate<String, Object> redisTemplate;
//    private final JsonObjectMapper jsonObjectMapper;
//
//    @Value("${spring.data.redis.channel.event_start}")
//    private String eventStartEventTopicName;
//
//    public void publish(EventStartEventDto event) {
//        String json = jsonObjectMapper.writeValueAsString(event);
//        redisTemplate.convertAndSend(eventStartEventTopicName, json);
//
//        log.info("Event start event sending with event id: {}, participants number: {}, has been sent",
//                event.getId(), event.getUserIds().size());
//    }
//}
