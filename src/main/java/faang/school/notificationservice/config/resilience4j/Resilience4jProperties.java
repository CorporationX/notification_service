package faang.school.notificationservice.config.resilience4j;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class Resilience4jProperties {
    public static final String DEFAULT_RETRY_CONFIG_NAME = "default-retry";
    public static final String DEFAULT_CIRCUIT_BREAKER_CONFIG_NAME = "default-circuit-breaker";
}
