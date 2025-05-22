package faang.school.notificationservice.config.redis;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.util.Map;

@Getter
@ConfigurationProperties(prefix = "spring.data.redis")
@RequiredArgsConstructor(onConstructor_ = @ConstructorBinding)
public class RedisConfig {
    private final int port;
    private final String host;
    private final Map<String, Channel> channels;

    public record Channel(String name) {
    }
}
