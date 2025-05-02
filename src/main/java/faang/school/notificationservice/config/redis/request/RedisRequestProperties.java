package faang.school.notificationservice.config.redis.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "spring.data.redis")
public class RedisRequestProperties {

    @NotBlank
    private String host;

    @NotNull
    private Integer port;

    private Channel channel = new Channel();

    @Data
    public static class Channel {

        @NotBlank
        private String transfer;
    }
}