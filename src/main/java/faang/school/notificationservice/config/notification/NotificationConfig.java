package faang.school.notificationservice.config.notification;

import com.vonage.client.VonageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class NotificationConfig {
    private final SmsProperties smsProperties;

    @Bean
    public VonageClient vonageClient() {
        return VonageClient.builder()
                .apiKey(smsProperties.getKey())
                .apiSecret(smsProperties.getSecret())
                .build();
    }
}
