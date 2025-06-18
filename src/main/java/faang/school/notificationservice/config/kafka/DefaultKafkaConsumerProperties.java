package faang.school.notificationservice.config.kafka;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.kafka.consumer")
public record DefaultKafkaConsumerProperties(
        String bootstrapServers,
        String groupId,
        int maxPollRecords,
        String offsetResetConfig
) {}
