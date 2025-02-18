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
    private String host;
    private Integer port;
    private String username;
    private String password;
    private String from;
}
