package faang.school.notificationservice.config.context;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "telegram")
public class TelegramProperties {

    @NotBlank
    private String botUsername;

    @NotBlank
    private String botToken;
}