package faang.school.notificationservice.config.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;


@Configuration
public class EmailConfig {

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private boolean smtpAuth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private boolean starttlsEnabled;

    @Value("${spring.mail.properties.mail.smtp.starttls.connection-timeout}")
    private int connectionTimeout;

    @Value("${spring.mail.properties.mail.smtp.starttls.timeout}")
    private int timeout;

    @Value("${spring.mail.properties.mail.smtp.starttls.write-timeout}")
    private int writeTimeout;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", smtpAuth);
        props.put("mail.smtp.starttls.enable", starttlsEnabled);
        props.put("mail.smtp.starttls.connection-timeout", connectionTimeout);
        props.put("mail.smtp.starttls.timeout", timeout);
        props.put("mail.smtp.starttls.write-timeout", writeTimeout);
        return mailSender;
    }
}