package faang.school.notificationservice.config.email;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "spring.mail")
public class MailProperties {

    @NotBlank
    private String host;

    @NotNull
    private Integer port;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String from;

    @NotBlank
    private String replyTo;

    @NotNull
    private Properties properties;

    @Data
    public static class Properties {

        @NotNull
        private Smtp smtp;

        @Data
        public static class Smtp {

            private boolean auth;

            @NotNull
            private Starttls starttls;

            @NotNull
            private Integer connectionTimeout;

            @NotNull
            private Integer timeout;

            @NotNull
            private Integer writeTimeout;

            @Data
            public static class Starttls {

                private boolean enable;
            }
        }
    }
}
