package faang.school.notificationservice.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.mail")
public class MailProperties {
    private String host;
    private int port;
    private String username;
    private String password;
    private Transport transport;
    private Properties properties;

    @Getter
    @Setter
    public static class Transport {
        private String protocol;
    }

    @Getter
    @Setter
    public static class Properties {
        private Smtp smtp;
    }

    @Getter
    @Setter
    public static class Smtp {
        private String auth;
        private Starttls starttls;
        private String debug;
        private int connectiontimeout;
        private int timeout;
        private int writetimeout;
    }

    @Getter
    @Setter
    public static class Starttls {
        private String enable;
    }
}
