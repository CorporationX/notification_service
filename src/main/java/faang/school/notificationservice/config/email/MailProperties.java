package faang.school.notificationservice.config.email;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.mail")
public class MailProperties {

    private String host;
    private int port;
    private String username;
    private String password;
    private String from;
    private String replyTo;
    private Properties properties = new Properties();;

    @Data
    public static class Properties {
        private Mail smtp = new Mail();

        @Data
        public static class Mail {
            private boolean auth;
            private Starttls starttls = new Starttls();
            private int connectiontimeout;
            private int timeout;
            private int writetimeout;

            @Data
            public static class Starttls {
                private boolean enable = true;
            }
        }
    }
}
