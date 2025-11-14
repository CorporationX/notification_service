package faang.school.notificationservice.config.context;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSendException;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class RetryConfig {
    @Value("${spring.mail.retry.max-attempts:3}")
    private int maxAttempts;

    @Value("${spring.mail.retry.delay:2000}")
    private long delay;

    @Value("${spring.mail.retry.multiplier:2}")
    private double multiplier;

    @Bean
    public RetryTemplate retryTemplate() {
        return RetryTemplate.builder()
                .maxAttempts(maxAttempts)
                .exponentialBackoff(delay, multiplier, delay * 10, true)
                .retryOn(MailSendException.class)
                .build();
    }
}
