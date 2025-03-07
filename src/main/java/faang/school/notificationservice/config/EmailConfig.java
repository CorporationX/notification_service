package faang.school.notificationservice.config;

import faang.school.notificationservice.properties.EmailProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class EmailConfig {

    private final EmailProperties emailProperties;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(emailProperties.getHost());
        mailSender.setPort(emailProperties.getPort());

        mailSender.setUsername(emailProperties.getUsername());
        mailSender.setPassword(emailProperties.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", emailProperties.getProperties().getMail().getTransport().getProtocol());
        props.put("mail.smtp.auth", emailProperties.getProperties().getMail().getSmtp().isAuth());
        props.put("mail.smtp.starttls.enable", emailProperties.getProperties().getMail().getSmtp().getStarttls());
        props.put("mail.debug", "true");

        return mailSender;
    }
}
