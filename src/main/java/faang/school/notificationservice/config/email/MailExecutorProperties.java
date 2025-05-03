package faang.school.notificationservice.config.email;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "executor.email")
public class MailExecutorProperties {

    @NotNull
    private Integer threadCoreSize;

    @NotNull
    private Integer maxPoolSize;

    @NotNull
    private Integer queueCapacity;
}
