package faang.school.notificationservice.config.vonage;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "vonage")
@Data
public class VonageProperties {
    private String apiKey;
    private String apiSecret;
    private String from;
    private String fromNumber;
}
