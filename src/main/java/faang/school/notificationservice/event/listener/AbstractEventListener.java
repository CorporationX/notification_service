package faang.school.notificationservice.event.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {

    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;

    public UserDto getUser(Long userId) {
        return Optional.ofNullable(userServiceClient.getUser(userId))
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
    }

    protected T parseEvent(String eventJson, Class<T> eventType) {
        try {
            return objectMapper.readValue(eventJson, eventType);
        } catch (Exception e) {
            log.error("Error parsing event: {}", eventJson, e);
            throw new RuntimeException("Failed to parse event", e);
        }
    }
}