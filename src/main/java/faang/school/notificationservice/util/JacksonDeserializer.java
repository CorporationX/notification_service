package faang.school.notificationservice.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.event.CommentEventDto;
import org.apache.kafka.common.serialization.Deserializer;

public class JacksonDeserializer implements Deserializer<CommentEventDto> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public CommentEventDto deserialize(String topic, byte[] data) {
        try {
            if (data == null) {
                return null;
            }
            return objectMapper.readValue(data, CommentEventDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing message", e);
        }
    }
}
