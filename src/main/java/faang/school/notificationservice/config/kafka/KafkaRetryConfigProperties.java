package faang.school.notificationservice.config.kafka;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kafka.retry")
public record KafkaRetryConfigProperties(
    int attempts,
    long backoffDelay
) {}

