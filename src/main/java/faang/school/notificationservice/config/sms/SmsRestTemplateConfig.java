package faang.school.notificationservice.config.sms;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@Configuration
public class SmsRestTemplateConfig {
    @Value("${spring.sms.exolve.connectionTimeoutSeconds}")
    private long connectionTimeoutSeconds;
    @Value("${spring.sms.exolve.readTimeoutSeconds}")
    private long readTimeoutSeconds;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.of(connectionTimeoutSeconds, ChronoUnit.SECONDS))
                .setReadTimeout(Duration.of(readTimeoutSeconds, ChronoUnit.SECONDS))
                .build();
    }
}
