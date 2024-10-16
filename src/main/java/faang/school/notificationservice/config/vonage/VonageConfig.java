package faang.school.notificationservice.config.vonage;

import com.vonage.client.VonageClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VonageConfig {
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
