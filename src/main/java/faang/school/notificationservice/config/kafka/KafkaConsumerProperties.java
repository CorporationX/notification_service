package faang.school.notificationservice.config.kafka;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.kafka.consumer")
public class KafkaConsumerProperties {

    private String bootstrapServers;
    private String keyDeserializer;
    private String valueDeserializer;
    private String groupId;
    private Map<String, Objects> properties;
}