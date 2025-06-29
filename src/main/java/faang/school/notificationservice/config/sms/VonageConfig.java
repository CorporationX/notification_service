package faang.school.notificationservice.config.sms;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "vonage.api")
public record VonageConfig(String key, String secret) {
}