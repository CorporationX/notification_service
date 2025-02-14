package faang.school.notificationservice.config.sms;

import com.vonage.client.VonageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SmsServiceConfiguration {

    private final SmsServiceProperties properties;

    @Bean
    public VonageClient vonageClient() {
        return VonageClient.builder()
                .apiKey(properties.getKey())
                .apiSecret(properties.getSecret())
                .build();
    }
}
