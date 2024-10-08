package faang.school.notificationservice.config.sms;

import com.vonage.client.VonageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SmsConfiguration {

    private final SmsConfig smsConfig;

    @Bean
    public VonageClient vonageClient() {
        return VonageClient.builder()
                .apiKey(smsConfig.getApiKey())
                .apiSecret(smsConfig.getApiSecret())
                .build();
    }
}