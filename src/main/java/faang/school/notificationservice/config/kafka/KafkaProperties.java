package faang.school.notificationservice.config.kafka;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@ConfigurationProperties(prefix = "spring.data.kafka")
public class KafkaProperties {
    private boolean useKafka;
    private String bootstrapServers;
    private String groupId;
    private String autoOffset;
    private int concurrency;
    private List<TopicConfig> topics;

    @Data
    public static class TopicConfig {
        private String name;
        private int partitions;
        private short replicationFactor;
    }
}
