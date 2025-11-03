package faang.school.notificationservice.config.vonage;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "vonage")
public class VonageProperties {
    private Api api = new Api();
    private String from;
    private boolean enabled = true;
    private String defaultCountry = "SG";

    @Data
    public static class Api {
        private String key;
        private String secret;
    }
}
