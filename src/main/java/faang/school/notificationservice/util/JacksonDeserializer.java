package faang.school.notificationservice.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.event.CommentEventDto;
import faang.school.notificationservice.dto.event.LikeEvent;
import faang.school.notificationservice.properties.EventType;
import faang.school.notificationservice.properties.KafkaProperties;
import jakarta.annotation.PostConstruct;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class JacksonDeserializer implements Deserializer<Object> {

    private final KafkaProperties kafkaProperties;
    private final ObjectMapper objectMapper;
    private final Map<String, Class<?>> topicToDtoClass = new HashMap<>();

    @Autowired
    public JacksonDeserializer(KafkaProperties kafkaProperties, ObjectMapper objectMapper) {
        this.kafkaProperties = kafkaProperties;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    private void init() {
        topicToDtoClass.put(
                kafkaProperties.getTopic(EventType.LIKED_POST),
                LikeEvent.class);
        topicToDtoClass.put(
                kafkaProperties.getTopic(EventType.COMMENT_CREATED),
                CommentEventDto.class);
    }

    @Override
    public Object deserialize(String topic, byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }
        Class<?> dtoClass = topicToDtoClass.get(topic);
        if (dtoClass == null) {
            throw new IllegalStateException("No DTO mapping for Kafka topic: " + topic);
        }
        try {
            return objectMapper.readValue(data, dtoClass);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing message from topic [" + topic + "]", e);
        }
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public void close() {
    }
}