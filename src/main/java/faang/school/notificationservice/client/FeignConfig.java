package faang.school.notificationservice.client;


import faang.school.notificationservice.config.context.UserContext;
import feign.Retryer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.concurrent.TimeUnit.SECONDS;

@Configuration
public class FeignConfig {

    @Value("${retry.retryer.period}")
    private int retryPeriod;

    @Value("${retry.retryer.duration}")
    private int retryDuration;

    @Value("${retry.retryer.max-attempts}")
    private int retryMaxAttempts;

    @Bean
    public FeignUserInterceptor feignUserInterceptor(UserContext userContext) {
        return new FeignUserInterceptor(userContext);
    }

    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default(retryPeriod, SECONDS.toMillis(retryDuration), retryMaxAttempts);
    }
}