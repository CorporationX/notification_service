package faang.school.notificationservice.config;

import com.vonage.client.VonageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class NotificationConfig {

    private final SmsParams smsParams;

    @Bean
    public VonageClient vonageClient() {
        return VonageClient.builder()
                .apiKey(smsParams.getKey())
                .apiSecret(smsParams.getSecret())
                .build();
    }
}
