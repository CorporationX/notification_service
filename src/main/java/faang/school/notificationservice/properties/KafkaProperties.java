package faang.school.notificationservice.properties;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.kafka.consumer")
public class KafkaProperties {
    private String bootstrapServers;
    private String groupId;
    private Map<String, String> topics;

    public String getTopic(@NonNull EventType eventType) {
        return topics.get(eventType.name());
    }
}