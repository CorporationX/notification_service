package faang.school.notificationservice.config.sms;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "vonage.api")
public record VonageProperties(String key, String secret) {
}
