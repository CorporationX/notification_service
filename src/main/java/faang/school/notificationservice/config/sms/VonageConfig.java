package faang.school.notificationservice.config.sms;

import com.vonage.client.VonageClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VonageConfig {

    @Bean
    public VonageClient vonageClient(VonageProperties properties) {
        return VonageClient.builder()
                .apiKey(properties.key())
                .apiSecret(properties.secret())
                .build();
    }
}
