package faang.school.notificationservice.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "vonage.api")
public record VonageProperties(
        String key,
        String secret,
        String from
) {
}
