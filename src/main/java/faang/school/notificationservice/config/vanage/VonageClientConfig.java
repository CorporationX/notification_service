package faang.school.notificationservice.config.vanage;

import com.vonage.client.VonageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class VonageClientConfig {

    private final VonageProperties vonageProperties;

    @Bean
    public VonageClient vonageClient() {
        return VonageClient.builder()
                .apiKey(vonageProperties.getKey())
                .apiSecret(vonageProperties.getSecret())
                .build();
    }
}
