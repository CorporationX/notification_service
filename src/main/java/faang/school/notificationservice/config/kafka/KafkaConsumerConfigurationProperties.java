package faang.school.notificationservice.config.kafka;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.data.kafka.consumer")
@Getter
@Setter
public class KafkaConsumerConfigurationProperties {
    private String host;
    private int port;
    private String groupId;
    private String autoOffsetReset;
}
