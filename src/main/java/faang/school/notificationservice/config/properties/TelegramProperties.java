package faang.school.notificationservice.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "telegram.bot")
public class TelegramProperties {
    private String token;
    private String username;
    private String linkTemplate;
}
