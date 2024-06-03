package faang.school.notificationservice.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "telegram.api")
public class TelegramBotProperty {
    private String username;
    private String token;
}
