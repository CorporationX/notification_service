package faang.school.notificationservice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventMapper<T> {

    private final ObjectMapper objectMapper;

    public T mapMessageToEvent(String message, Class<T> eventType) {
        try {
            return objectMapper.readValue(message, eventType);
        } catch (JsonProcessingException e) {
            log.error("Failed map message to Event");
            throw new RuntimeException(e);
        }
    }
}