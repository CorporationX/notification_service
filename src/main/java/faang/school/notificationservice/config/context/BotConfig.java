package faang.school.notificationservice.config.context;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="telegram.bot")
@Data
public class BotConfig {

    private String username;
    private String token;
}