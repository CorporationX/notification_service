package faang.school.notificationservice.client;

import faang.school.notificationservice.config.sms.ProstorSmsProperty;
import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProstoSmsFeignConfig {
    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor(ProstorSmsProperty property) {
        return new BasicAuthRequestInterceptor(property.login(), property.password());
    }
}
