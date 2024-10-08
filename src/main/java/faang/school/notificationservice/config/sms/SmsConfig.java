package faang.school.notificationservice.config.sms;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class SmsConfig {

    @NotNull
    @Value("${vonage.api.key}")
    private String apiKey;

    @NotNull
    @Value("${vonage.api.secret}")
    private String apiSecret;

    @NotNull
    @Value("${vonage.api.sender}")
    private String sender;
}