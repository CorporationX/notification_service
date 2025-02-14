package faang.school.notificationservice.config.email;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.mail")
public class MailProperties {
    String host;
    Integer port;
    String username;
    String password;
    String replyTo;
}
