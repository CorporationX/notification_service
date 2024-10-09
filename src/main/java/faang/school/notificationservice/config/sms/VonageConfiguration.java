package faang.school.notificationservice.config.sms;

import com.vonage.client.VonageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(VonageConfigurationProperties.class)
public class VonageConfiguration {

    private final VonageConfigurationProperties vonageConfigurationProperties;

    @Bean
    public VonageClient vonageClient() {
        return VonageClient.builder()
                .apiKey(vonageConfigurationProperties.getKey())
                .apiSecret(vonageConfigurationProperties.getSecret())
                .build();
    }
}