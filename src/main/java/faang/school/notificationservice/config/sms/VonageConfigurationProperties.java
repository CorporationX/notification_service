package faang.school.notificationservice.config.sms;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "vonage.api")
public class VonageConfigurationProperties {

    @NotNull
    @Value("${vonage.api.key}")
    private String key;

    @NotNull
    @Value("${vonage.api.secret}")
    private String secret;

    @NotNull
    @Value("${vonage.api.sender}")
    private String sender;
}