package faang.school.notificationservice.config.email;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.mail")
public record EmailSenderConfig(
        String username,
        String password
) {
}
