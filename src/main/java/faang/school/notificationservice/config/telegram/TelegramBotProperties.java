package faang.school.notificationservice.config.telegram;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("spring.telegram-bot")
public class TelegramBotProperties {
    private String token;
    private String name;
    private String defaultStickerId;
}
