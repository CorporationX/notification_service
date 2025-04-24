package faang.school.notificationservice.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.event.AchievementEventDto;
import org.apache.kafka.common.serialization.Deserializer;

public class JacksonDeserializer implements Deserializer<AchievementEventDto> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public AchievementEventDto deserialize(String topic, byte[] data) {
        try {
            if (data == null) {
                return null;
            }
            return objectMapper.readValue(data, AchievementEventDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing message", e);
        }
    }
}
