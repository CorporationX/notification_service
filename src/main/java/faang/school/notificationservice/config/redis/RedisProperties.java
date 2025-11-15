package faang.school.notificationservice.config.redis;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Setter
@Getter
@Component
@ConfigurationProperties("spring.redis")
@Validated
public class RedisProperties {
    @NotBlank(message = "Redis host cannot be null")
    private String host;
    @Positive(message = "Redis port cannot be negative")
    private int port;
    @NotBlank(message = "Redis password cannot be null")
    private String password;
}