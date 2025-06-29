package faang.school.notificationservice.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "exolve")
public class ExolveProperties {

    private Sms sms = new Sms();
    private Api api = new Api();

    @Getter
    @Setter
    public static class Sms {
        private String uri;
        private String serviceNumber;
    }

    @Getter
    @Setter
    public static class Api {
        private String key;
    }

}
