package faang.school.notificationservice.config.telegram;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "telegram-bot")
public class BotProperties {
    private String token;
    private String name;
}
