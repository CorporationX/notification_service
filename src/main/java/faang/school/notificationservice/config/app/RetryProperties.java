package faang.school.notificationservice.config.app;

import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "retry")
@Data
@Validated
public class RetryProperties {
    @Min(1)
    private int attempts;
    @Min(100)
    private int delay;
}
