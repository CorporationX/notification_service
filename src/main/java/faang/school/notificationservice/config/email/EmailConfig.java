package faang.school.notificationservice.config.email;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;


@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "mail")
public class EmailConfig {
    private final static String protocol = "smtp";

    @Getter
    @Setter
    public static class PropertiesConfig {
        EmailPropertiesConfig mail;
    }

    @Getter
    @Setter
    public static class EmailPropertiesConfig {
        private Smtp smtp;
        private String debug;
    }

    @Getter
    @Setter
    public static class Smtp {
        private String auth;
        private String starttlsEnable;
        private int connectionTimeout;
        private int timeout;
        private int writeTimeout;
    }

    private String host;
    private int port;
    private String username;
    private String password;
    private PropertiesConfig properties;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);

        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", protocol);
        properties.put("mail.smtp.auth", this.properties.mail.smtp.auth);
        properties.put("mail.smtp.starttls.enable", this.properties.mail.smtp.starttlsEnable);
        properties.put("mail.debug", this.properties.mail.debug);

        return mailSender;
    }
}
