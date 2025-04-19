package faang.school.notificationservice.config.telegram;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурационный класс для настроек Telegram бота.
 * Содержит свойства, необходимые для работы с Telegram API,
 * такие как имя бота и его токен доступа.
 * Значения свойств загружаются из конфигурационного файла приложения.
 */
@Configuration
@Getter
public class TelegramConfig {

    @Value("${telegram.bot.name}")
    private String botName;

    @Value("${telegram.bot.token}")
    private String botToken;
}