package faang.school.notificationservice.config.vonage;

import com.vonage.client.VonageClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(VonageProperties.class)
public class VonageConfig {

    @Bean
    public VonageClient vonageClient(VonageProperties props
    ) {
        return VonageClient.builder()
                .apiKey(props.getApiKey())
                .apiSecret(props.getApiSecret())
                .build();
    }
}
