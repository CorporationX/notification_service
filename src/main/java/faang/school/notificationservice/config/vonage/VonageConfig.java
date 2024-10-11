package faang.school.notificationservice.config.vonage;

import com.vonage.client.VonageClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@Configuration
public class VonageConfig {
    @Value("${vonage.api.key}")
    private String vonageApiKey;

    @Value("${vonage.api.secret}")
    private String vonageApiSecret;

    @Bean
    public VonageClient vonageClient() {
        return VonageClient.builder()
                .apiKey(vonageApiKey)
                .apiSecret(vonageApiSecret)
                .build();
    }
}
