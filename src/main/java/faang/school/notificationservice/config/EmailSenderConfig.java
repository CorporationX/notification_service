package faang.school.notificationservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailSenderConfig {

    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.username}")
    private String senderName;
    @Value("${spring.mail.password}")
    private String senderPassword;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private boolean smptAuthEnable;
    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private boolean smptTTLEnable;


    @Value("${spring.mail.properties.mail.smtp.connectiontimeout}")
    private int connectionTimeout;
    @Value("${spring.mail.properties.mail.smtp.timeout}")
    private int timeout; // read timout
    @Value("${spring.mail.properties.mail.smtp.writetimeout}")
    private int writeTimeout;

    @Value("${spring.mail.properties.mail.smtp.debug}")
    private boolean debug;

    @Bean
    String notificationServiceEmail() {
        return senderName;
    }

    @Bean
    @Primary
    JavaMailSender mailSender() {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(host);
        mailSender.setPort(port);

        mailSender.setUsername(senderName);
        mailSender.setPassword(senderPassword);

        Properties props = mailSender.getJavaMailProperties();

        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", smptAuthEnable);
        props.put("mail.smtp.starttls.enable", smptTTLEnable);

        props.put("mail.smtp.connectiontimeout", connectionTimeout);
        props.put("mail.smtp.timeout", timeout);
        props.put("mail.smtp.writetimeout", writeTimeout);

        props.put("mail.smtp.debug", debug);

        return mailSender;
    }
}
