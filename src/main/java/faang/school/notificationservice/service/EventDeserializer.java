package faang.school.notificationservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.exception.EventDeserializationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class EventDeserializer {

    private final ObjectMapper objectMapper;

    public <T> T deserialize(String json, Class<T> eventClass) {
        try {
            T event = objectMapper.readValue(json, eventClass);
            log.debug("Successfully deserialized event of type {}", eventClass.getSimpleName());
            return event;
        } catch (Exception e) {
            log.error("Failed to deserialize event: {}", json, e);
            throw new EventDeserializationException("Failed to deserialize event", e);
        }
    }
}