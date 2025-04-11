package faang.school.notificationservice.config.redis;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Data
@Component
@Validated
@ConfigurationProperties(prefix = "spring.data.redis")
public class RedisProperties {

    @NotBlank(message = "Redis host must not be blank")
    private String host;

    @NotNull(message = "Redis port must not be null")
    @Min(value = 1, message = "Redis port must be at least 1")
    @Max(value = 65535, message = "Redis port must not exceed 65535")
    private Integer port;

    private Channel channel;

    @Data
    public static class Channel {

        @NotBlank(message = "Follower channel must not be blank")
        private String follower;

        @NotBlank(message = "Unfollower channel must not be blank")
        private String unfollower;
    }
}
