package faang.school.notificationservice.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.data.kafka")
public record KafkaProperties(
        String bootstrapServers,
        Consumer consumer,
        Producer producer
) {
    public record Consumer(
            String groupId,
            String autoOffsetReset
    ) {}

    public record Producer(
            String acks
    ) {}

}
