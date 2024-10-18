package faang.school.notificationservice.config.vonage;

import com.vonage.client.VonageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class VonageConfig {

    private final VonageProperties vonageProperties;

    @Bean
    public VonageClient vonageClient() {
        return VonageClient.builder()
                .apiKey(vonageProperties.getApi().getKey())
                .apiSecret(vonageProperties.getApi().getKey())
                .build();
    }
}

