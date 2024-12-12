package faang.school.notificationservice.config.notification;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.smsaero.SmsAero;

@Configuration
public class SmsConfig {

    @Value("${smsaero.api.key}")
    String key;

    @Value("${smsaero.api.email}")
    String email;

    @Bean
    public SmsAero smsAeroClient() {
        return new SmsAero(email, key);
    }
}
