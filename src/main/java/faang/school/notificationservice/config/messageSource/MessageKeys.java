package faang.school.notificationservice.config.messageSource;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "messages.keys")
@Data
public class MessageKeys {
    private String likePost;
}
