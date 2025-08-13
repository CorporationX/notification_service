package faang.school.notificationservice.config.sms;

import com.vonage.client.VonageClient;
import faang.school.notificationservice.config.properties.VonageProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(VonageProperties.class)
@RequiredArgsConstructor
public class VonageConfig {

    private final VonageProperties props;

    @Bean
    public VonageClient vonageClient() {
        return VonageClient.builder()
                .apiKey(props.key())
                .apiSecret(props.secret())
                .build();
    }
}
