package faang.school.notificationservice.config.sms;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.smsaero.SmsAero;

@Configuration
public class SmsAeroConfig {

    @Value("${sms-aero.api-key}")
    private String apiKey;

    @Value("${sms-aero.email}")
    private String email;

    @Bean
    public SmsAero smsAero() {
        return new SmsAero(email, apiKey);
    }
}
