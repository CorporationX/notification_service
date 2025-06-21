package faang.school.notificationservice.config.kafka;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.data.kafka.consumer")
@Component
public class KafkaConsumerConfigurationProperties {
    private String groupId;
    private String autoOffsetReset;
}
