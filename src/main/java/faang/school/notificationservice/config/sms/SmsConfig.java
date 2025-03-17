package faang.school.notificationservice.config.sms;

import com.vonage.client.VonageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SmsConfig {

    @Value("${vonage.api.key}")
    private String key;

    @Value("${vonage.api.secret}")
    private String secret;

    @Bean
    public VonageClient vonageClient() {
        return VonageClient.builder()
                .apiKey(key)
                .apiSecret(secret)
                .build();
    }
}
