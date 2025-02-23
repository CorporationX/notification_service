package faang.school.notificationservice.config.sms;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("spring.sms.exolve")
public class SmsConnectionParam {
    private String url;
    private String authorizationString;
    private String sourcePhoneNumber;
}
