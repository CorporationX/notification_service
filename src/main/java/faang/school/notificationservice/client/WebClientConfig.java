package faang.school.notificationservice.client;

import faang.school.notificationservice.config.provider.SmsRuProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }

    @Bean
    public WebClient smsRuWebClient(SmsRuProperties properties) {
        return WebClient.builder()
                .baseUrl(properties.getUrl())
                .build();
    }
}
