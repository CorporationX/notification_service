package faang.school.notificationservice.config;

import faang.school.notificationservice.properties.MailProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class MailSenderConf {
    private final MailProperties mailProperties;

    @Bean
    public JavaMailSender emailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailProperties.getHost());
        mailSender.setPort(mailProperties.getPort());

        mailSender.setUsername(mailProperties.getUsername());
        mailSender.setPassword(mailProperties.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", mailProperties.getTransport().getProtocol());
        props.put("mail.smtp.auth", mailProperties.getProperties().getSmtp().getAuth());
        props.put("mail.smtp.starttls.enable", mailProperties.getProperties().getSmtp().getStarttls().getEnable());
        props.put("mail.debug",  mailProperties.getProperties().getSmtp().getDebug());

        return mailSender;
    }
}
