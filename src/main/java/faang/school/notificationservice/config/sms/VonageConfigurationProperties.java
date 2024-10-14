package faang.school.notificationservice.config.sms;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
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