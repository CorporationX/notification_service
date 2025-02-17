package faang.school.notificationservice.config.sms;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@Configuration
@ConfigurationProperties(prefix = "vonage.api")
public class SmsServiceProperties {

    @NotBlank
    private String key;

    @NotBlank
    private String secret;

    @NotBlank
    private String from;
}
