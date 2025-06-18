package faang.school.notificationservice.client.vonageclient;

import com.vonage.client.VonageClient;
import faang.school.notificationservice.config.SmsProperties;
import faang.school.notificationservice.exception.SmsServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class VonageClientConfig {
    private static final String NO_CREDENTIALS = "Vonage API credentials are not configured.";
    private final SmsProperties smsProperties;

    @Bean
    public VonageClient client() {
        if (smsProperties.getKey() == null || smsProperties.getSecret() == null) {
            log.error(NO_CREDENTIALS);
            throw new SmsServiceException(NO_CREDENTIALS);
        }
        return VonageClient.builder()
                .apiKey(smsProperties.getKey())
                .apiSecret(smsProperties.getSecret())
                .build();
    }

}
