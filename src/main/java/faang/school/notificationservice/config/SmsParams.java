package faang.school.notificationservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "vonage.api")
public class SmsParams {
    private String key;
    private String secret;
}