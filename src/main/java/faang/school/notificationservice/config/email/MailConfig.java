package faang.school.notificationservice.config.email;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class MailConfig {

    private final MailProperties mailProperties;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(mailProperties.getHost());
        mailSender.setPort(mailProperties.getPort());
        mailSender.setUsername(mailProperties.getUsername());
        mailSender.setPassword(mailProperties.getPassword());
        mailSender.setProtocol("smtp");
        mailSender.setDefaultEncoding("UTF-8");

        Properties properties = getProperties(mailSender);

        mailSender.setJavaMailProperties(properties);

        return mailSender;
    }

    private Properties getProperties(JavaMailSenderImpl mailSender) {
        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.smtp.auth", mailProperties.getProperties().getSmtp().isAuth());
        properties.put("mail.smtp.starttls.enable", mailProperties.getProperties().getSmtp().getStarttls().isEnable());
        properties.put("mail.smtp.connectiontimeout", mailProperties.getProperties().getSmtp().getConnectiontimeout());
        properties.put("mail.smtp.timeout", mailProperties.getProperties().getSmtp().getTimeout());
        properties.put("mail.smtp.writetimeout", mailProperties.getProperties().getSmtp().getWritetimeout());
        return properties;
    }
}
