package faang.school.notificationservice.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import faang.school.notificationservice.dto.event.CommentEventDto;
import faang.school.notificationservice.dto.event.LikeEvent;
import faang.school.notificationservice.properties.EventType;
import faang.school.notificationservice.properties.KafkaProperties;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class JacksonDeserializer implements Deserializer<Object> {

    private final KafkaProperties kafkaProperties;
    private final ObjectMapper objectMapper;

    private final Map<String, Class<?>> topicToDtoClass = new HashMap<>();

    public JacksonDeserializer(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;

        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());

        this.objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @PostConstruct
    private void init() {
        String likedTopic = kafkaProperties.getTopic(EventType.LIKED_POST);
        topicToDtoClass.put(likedTopic, LikeEvent.class);

        String commentTopic = kafkaProperties.getTopic(EventType.COMMENT_CREATED);
        topicToDtoClass.put(commentTopic, CommentEventDto.class);
    }

    @Override
    public Object deserialize(String topic, byte[] data) {
        try {
            if (data == null) {
                return null;
            }

            Class<?> dtoClass = topicToDtoClass.get(topic);
            if (dtoClass != null) {
                return objectMapper.readValue(data, dtoClass);
            }

            return objectMapper.readValue(data, CommentEventDto.class);

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