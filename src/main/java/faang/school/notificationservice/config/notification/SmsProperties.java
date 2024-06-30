package faang.school.notificationservice.config.notification;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "vonage.api")
public class SmsProperties {
    @NotNull
    private String key;
    @NotNull
    private String secret;
    @NotNull
    @Value("${brand.name}")
    private String brandName;
}
