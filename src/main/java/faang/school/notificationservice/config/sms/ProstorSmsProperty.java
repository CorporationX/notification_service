package faang.school.notificationservice.config.sms;

import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sms.prostorsms.api")
public record ProstorSmsProperty(
        @NonNull String login,
        @NonNull String password
) {}
