package faang.school.notificationservice.config.kafka;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "spring.kafka.consumer")
public record ConsumerProperty(
        @DefaultValue("events") String groupId,
        @DefaultValue("earliest") String autoOffsetReset
) {
}
