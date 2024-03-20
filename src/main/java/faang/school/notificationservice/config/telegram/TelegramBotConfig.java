package faang.school.notificationservice.config.telegram;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@PropertySource("application.yaml")
public class TelegramBotConfig {

    @Value("${spring.telegram.botName}")
    private String botName;
    @Value("${spring.telegram.botToken}")
    private String botToken;
}

