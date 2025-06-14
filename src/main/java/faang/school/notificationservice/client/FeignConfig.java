package faang.school.notificationservice.client;

import faang.school.notificationservice.config.context.UserContext;
import feign.Logger;
import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public FeignUserInterceptor feignUserInterceptor(UserContext userContext) {
        return new FeignUserInterceptor(userContext);
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;  // FULL, BASIC, HEADERS, NONE
    }

    @Bean
    public BasicAuthRequestInterceptor basicAuthenticationInterceptor() {
        return new BasicAuthRequestInterceptor("t89994676160", "504421");
    }
}
