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
    private final EmailProperties emailConfig;
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
        mailSender.setHost(emailConfig.getHost());
        mailSender.setPort(emailConfig.getPort());
        mailSender.setUsername(emailConfig.getUsername());
        mailSender.setPassword(emailConfig.getPassword());

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.smtp.auth", emailConfig.getAuth());
        properties.put("mail.smtp.starttls.enable", emailConfig.getEnable());
        properties.put("mail.smtp.connectiontimeout", emailConfig.getConnectionTimeout());
        properties.put("mail.smtp.timeout", emailConfig.getTimeout());
        properties.put("mail.smtp.writetimeout", emailConfig.getWriteTimeout());

        return mailSender;
    }
}
