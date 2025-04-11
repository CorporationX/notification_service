package faang.school.notificationservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Класс свойств конфигурации для подключения к Redis.
 * <p>
 * Связывает свойства из конфигурационного файла (application.yml)
 * с Java-объектом для удобного использования в коде. Все свойства должны иметь префикс
 * {@code spring.data.redis} в конфигурационных файлах.
 * </p>
 */
@Data
@ConfigurationProperties(prefix = "spring.data.redis")
public class RedisProperties {
    private String host;
    private int port;
    private int connectTimeout;
    private int readTimeout;
}