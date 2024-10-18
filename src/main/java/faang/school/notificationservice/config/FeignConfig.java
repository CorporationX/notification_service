package faang.school.notificationservice.config;


import faang.school.notificationservice.config.context.FeignUserInterceptor;
import faang.school.notificationservice.config.context.UserContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public FeignUserInterceptor feignUserInterceptor(UserContext userContext) {
        return new FeignUserInterceptor(userContext);
    }
}
