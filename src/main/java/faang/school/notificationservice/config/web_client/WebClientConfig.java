package faang.school.notificationservice.config.web_client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Конфигурационный класс для WebClient
 *
 * @author Linempy
 * @since 12.08.2025
 */
@Configuration
public class WebClientConfig {

    @Value("${sms.prostor.url}")
    private String url;

    @Bean
    public WebClient webClient() {
        return WebClient.create(url);
    }
}