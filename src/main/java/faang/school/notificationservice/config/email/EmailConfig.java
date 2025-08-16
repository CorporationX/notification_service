package faang.school.notificationservice.config.email;

import faang.school.notificationservice.config.properties.email.EmailProperties;
import faang.school.notificationservice.config.properties.email.EmailProtocolProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(EmailProperties.class)
public class EmailConfig {

    private final EmailProperties emailProperties;
    private final EmailProtocolProperties protocolProperties;


    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(emailProperties.host());
        mailSender.setPort(emailProperties.port());
        mailSender.setUsername(emailProperties.username());
        mailSender.setPassword(emailProperties.password());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", protocolProperties.getProtocol());
        props.put("mail.smtp.auth", protocolProperties.isAuth());
        props.put("mail.smtp.starttls.enable", protocolProperties.isStarttlsEnabled());

        return mailSender;
    }
}
