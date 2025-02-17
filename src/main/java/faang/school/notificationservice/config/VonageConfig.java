package faang.school.notificationservice.config;

import com.vonage.client.VonageClient;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class VonageConfig {
    @Value("${vonage.api.key}")
    private String key;
    @Value("${vonage.api.secret}")
    private String secret;
    @Value("${vonage.api.from}")
    private String from;

    @Bean
    public VonageClient getVonageClient() {
        return VonageClient.builder()
                .apiKey(key)
                .apiSecret(secret)
                .build();
    }
}
