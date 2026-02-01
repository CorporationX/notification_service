package faang.school.notificationservice.config;

import faang.school.notificationservice.SmsApi.SmsSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SmsConfiguration {

    @Bean
    public SmsSender smsSender(
            @Value("${mainsms.api.project}") String projectName,
            @Value("${mainsms.api.apiKey}") String apiKey
    ) {
        return new SmsSender(projectName, apiKey);
    }
}
