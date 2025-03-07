package faang.school.notificationservice.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.mail")
public class EmailProperties {

    private String host;

    private int port;

    private String username;

    private String password;

    private Properties properties;

    @Data
    public static class Properties {

        private Mail mail;

        @Data
        public static class Mail {

            private Transport transport;
            private Smtp smtp;

            @Data
            public static class Transport {

                private String protocol;
            }

            @Data
            public static class Smtp {

                private boolean auth;
                private Starttls starttls;

                @Data
                public static class Starttls {

                    private boolean enable;
                }
            }
        }
    }
}