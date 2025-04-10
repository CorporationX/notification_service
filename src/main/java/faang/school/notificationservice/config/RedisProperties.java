package faang.school.notificationservice.config.context;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Настройки Redis из конфигурации
 */
@ConfigurationProperties(prefix = "spring.data.redis")
@Data
public class RedisProperties {
    private String host;
    private int port;
    private int connectTimeout = 5000;
    private int readTimeout = 3000;

    private Listener listener = new Listener();

    @Data
    public static class Listener {
        private int taskThreads = 4;
        private int subscriptionThreads = 2;
    }
}
