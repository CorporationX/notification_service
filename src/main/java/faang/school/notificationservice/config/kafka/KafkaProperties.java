package faang.school.notificationservice.config.kafka;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "spring.data.kafka")
public class KafkaProperties {
    private boolean useKafka;
    private String bootstrapServers;
    private String groupId;
    private String autoOffset;
    private int concurrency;
}
