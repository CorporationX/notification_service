package faang.school.notificationservice.config.sms;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "vonage.api")
public class VonageConfigurationProperties {

    @NotNull
    private String key;

    @NotNull
    private String secret;

    @NotNull
    private String sender;
}