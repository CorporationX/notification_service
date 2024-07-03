package faang.school.notificationservice.config.notification;

import com.vonage.client.VonageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class NotificationConfig {
    private final SmsProperties smsProperties;
    private final EmailProperties emailProperties;

    @Bean
    public VonageClient vonageClient() {
        return VonageClient.builder()
                .apiKey(smsProperties.getKey())
                .apiSecret(smsProperties.getSecret())
                .build();
    }

    @Bean
    public JavaMailSender configureMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(emailProperties.getHost());
        mailSender.setPort(emailProperties.getPort());
        mailSender.setUsername(emailProperties.getUsername());
        mailSender.setPassword(emailProperties.getUserAppPassword());

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.smtp.auth", emailProperties.getAuthSmtp());
        properties.put("mail.smtp.starttls.enable", emailProperties.getStarttlsEnable());
        properties.put("mail.smtp.connectiontimeout", emailProperties.getConnectionTimeout());
        properties.put("mail.smtp.timeout", emailProperties.getTimeout());
        properties.put("mail.smtp.writetimeout", emailProperties.getWriteTimeout());

        return mailSender;
    }
}
