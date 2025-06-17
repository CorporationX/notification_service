package faang.school.notificationservice.service.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

public class EmailConfig {

    @Value("spring.mail.host")
     String host;
    @Value("spring.mail.port")
     int port;
    @Value("spring.mail.username")
     String emailAddress;
    @Value("spring.mail.password")
     String password;
    @Value("spring.mail.transport.protocol")
     boolean protocol;
    @Value("spring.mail.properties.mail.smtp.auth")
     boolean auth;
    @Value("spring.mail.properties.mail.smtp.starttls.enable")
     boolean starttls;
    @Value("spring.mail.debug")
     boolean debug;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);

        mailSender.setUsername(emailAddress);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", protocol);
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", starttls);
        props.put("mail.debug", debug);
        return mailSender;
    }
}
