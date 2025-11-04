package faang.school.notificationservice.config.vonage;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@ConfigurationProperties(prefix = "vonage")
public class VonageProperties {
    private String apiKey;
    private String apiSecret;
    private String from;
    private boolean enabled = true;
    private String defaultCountry = "SG";
    private Map<String, String> countryCodes = Map.of(
            "SG", "+65",
            "US", "+1",
            "GB", "+44",
            "IN", "+91",
            "CN", "+86",
            "RU", "+7"
    );
}

