package faang.school.notificationservice.config.vonage;

import com.vonage.client.VonageClient;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class VonageConfig {

    @Value("${vonage.api.key}")
    private String apiKey;

    @Value("${vonage.api.secret}")
    private String apiSecret;

    @Value("${vonage.brand-number}")
    private String brandNumber;

    @Value("${vonage.callback-uri}")
    private String callbackUrl;

    @Bean
    public VonageClient getVonageClient() {
        return VonageClient.builder()
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .build();
    }
}
