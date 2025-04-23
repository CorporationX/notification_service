package faang.school.notificationservice.properties;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "telegram")
public class TelegramBotProperties {

    @NotBlank
    private String botUsername;

    @NotBlank
    private String botToken;
}