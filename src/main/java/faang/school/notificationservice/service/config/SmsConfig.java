package faang.school.notificationservice.service.config;

import com.vonage.client.VonageClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SmsConfig {
    @Bean
    public VonageClient vonageClient() {
        return VonageClient.builder()
                .apiKey("ea3cbdef")
                .apiSecret("t6qqwTma9etW5JwR")
                .build();
    }
}
