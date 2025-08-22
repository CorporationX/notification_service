package faang.school.notificationservice.config.kafka;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "spring.kafka.backoff")
public record BackOffProperty(
        @DefaultValue("1000") long initInterval,
        @DefaultValue("2") int maxRetries,
        @DefaultValue("5000") long maxInterval,
        @DefaultValue("2") long multiplier
) {
}
