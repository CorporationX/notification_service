package faang.school.notificationservice.config.provider;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties(SmsRuProperties.class)
public class SmsRuConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
