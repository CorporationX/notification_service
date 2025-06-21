package faang.school.notificationservice.config.kafka;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.data.kafka")
@Component
public class KafkaConfigurationProperties {
    private String host;
    private int port;
}
