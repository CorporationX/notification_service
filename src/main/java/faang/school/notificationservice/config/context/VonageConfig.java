package faang.school.notificationservice.config.context;

import com.vonage.client.VonageClient;
import io.lettuce.core.dynamic.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VonageConfig {
    @Value("${vonage.api-key}")
    private String apiKey;

    @Value("${vonage.api-secret}")
    private String apiSecret;
//?

    public VonageConfig(String apiKey, String apiSecret) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }
    @Bean
    public static VonageClient createVonageClient() {
        return VonageClient.builder()
                .apiKey("ec1a2ee7")
                .apiSecret("HPRDmtd9nEOsdhqh")
                .build();
    }
}
