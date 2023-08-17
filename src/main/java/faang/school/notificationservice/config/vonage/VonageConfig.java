package faang.school.notificationservice.config.vonage;

import com.vonage.client.VonageClient;
import faang.school.notificationservice.service.sms_sending.SmsService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(VonageProperties.class)
public class VonageConfig {
    @Bean
    public VonageClient vonageClient(VonageProperties vonageProperties) {
        return VonageClient.builder()
                .apiKey(vonageProperties.getApiKey())
                .apiSecret(vonageProperties.getApiSecret())
                .build();
    }

    @Bean
    public SmsService smsService(VonageClient vonageClient,
                                 VonageProperties vonageProperties) {
        return new SmsService(vonageClient, vonageProperties.getFrom(), vonageProperties.getFromNumber());
    }
}
