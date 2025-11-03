package faang.school.notificationservice.config.vonage;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@ConfigurationProperties(prefix = "vonage")
public class VonageProperties {
    private Api api = new Api();
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

    @Data
    public static class Api {
        private String key;
        private String secret;
    }
}
