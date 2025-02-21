package faang.school.notificationservice.builder;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.LikePostEvent;
import org.springframework.stereotype.Component;

@Component
public class LikePostEventBuilder implements EventBuilder<LikePostEvent> {
    @Override
    public LikePostEvent build(String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(message, LikePostEvent.class);
    }
}
