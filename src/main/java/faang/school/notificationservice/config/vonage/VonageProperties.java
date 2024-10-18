package faang.school.notificationservice.config.vonage;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "vonage")
public class VonageProperties {
    private Api api;
    private String from;
}
