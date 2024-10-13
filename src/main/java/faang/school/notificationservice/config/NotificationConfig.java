package faang.school.notificationservice.config;

import com.vonage.client.VonageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.retry.annotation.EnableRetry;

@Configuration
@EnableRetry
@ConfigurationPropertiesScan
@RequiredArgsConstructor
public class NotificationConfig {

    private final SmsParams smsParams;

    @Bean
    public VonageClient vonageClient() {
        return VonageClient.builder()
                .apiKey(smsParams.getKey())
                .apiSecret(smsParams.getSecret())
                .build();
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
