package faang.school.notificationservice.config.email;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "executor.email")
public class MailExecutorProperties {

    @NotNull
    private int threadCoreSize;

    @NotNull
    private int maxPoolSize;

    @NotNull
    private int queueCapacity;
}
