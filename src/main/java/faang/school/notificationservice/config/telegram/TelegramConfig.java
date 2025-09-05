package faang.school.notificationservice.config.telegram;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@Data
public class TelegramConfig {
    @Value("${telegram.bot.username}")
    String username;
    @Value("${telegram.bot.token}")
    String token;
}
