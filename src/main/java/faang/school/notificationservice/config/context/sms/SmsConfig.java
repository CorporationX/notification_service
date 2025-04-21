package faang.school.notificationservice.config.context.sms;

import com.vonage.client.VonageClient;
import faang.school.notificationservice.service.provider.SmsProvider;
import faang.school.notificationservice.service.provider.VonageSmsProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SmsConfig {

    @Bean
    public VonageClient vonageClient(
            @Value("${vonage.api.key}") String key,
            @Value("${vonage.api.secret}") String secret
    ) {
        return VonageClient.builder()
                .apiKey(key)
                .apiSecret(secret)
                .build();
    }

    @Bean
    @ConditionalOnProperty(name = "sms.provider", havingValue = "vonage")
    public SmsProvider vonageSmsProvider(VonageClient vonageClient) {
        return new VonageSmsProvider(vonageClient);
    }
}
