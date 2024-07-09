package faang.school.notificationservice.service.telegram;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("telegram_config.properties")
public class BotConfig {
    @Value("${bot.name}") String botName;
    @Value("${bot.token}") String botToken;
}
