package faang.school.notificationservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.retry.interceptor.RetryInterceptorBuilder;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

public class RetryConfig {
    @Bean
    public RetryOperationsInterceptor retryInterceptor(RetryProperties retryProperties) {
        return RetryInterceptorBuilder.stateless()
                .maxAttempts(retryProperties.getMaxAttempts())
                .backOffOptions(
                        retryProperties.getInitialDelay(),
                        retryProperties.getMultiplier(),
                        retryProperties.getMaxDelay()
                )
                .build();
    }
}
