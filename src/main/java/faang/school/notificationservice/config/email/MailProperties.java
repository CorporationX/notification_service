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

    private Properties properties = new Properties();

    @Data
    public static class Properties {

        private Mail smtp = new Mail();

        @Data
        public static class Mail {

            private boolean auth;

            private Starttls starttls = new Starttls();

            @NotNull
            private Integer connectiontimeout;

            @NotNull
            private Integer timeout;

            @NotNull
            private Integer writetimeout;

            @Data
            public static class Starttls {

                private boolean enable = true;
            }
        }
    }
}
