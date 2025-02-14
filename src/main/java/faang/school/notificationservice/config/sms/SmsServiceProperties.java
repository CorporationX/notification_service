package faang.school.notificationservice.config.sms;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "vonage.api")
public class SmsServiceProperties {

    @NotBlank
    private String key;

    @NotBlank
    private String secret;
}
