package faang.school.notificationservice.config.app;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "telegram")
@Data
@Validated
public class TelegramProperties {
    @NotNull
    private String username;

    @NotNull
    private String token;

    @NotNull
    private  int timeout;
}
