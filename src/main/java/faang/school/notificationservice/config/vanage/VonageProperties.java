package faang.school.notificationservice.config.vanage;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "vonage")
public class VonageProperties {
    private String key;
    private String secret;
    private String from;
}
