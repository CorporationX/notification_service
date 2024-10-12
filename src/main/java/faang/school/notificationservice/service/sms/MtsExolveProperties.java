package faang.school.notificationservice.service.sms;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class MtsExolveProperties {
    @Value("${mts_exolve.apikey}")
    private String apiKey;
    @Value("${mts_exolve.uri}")
    private String apiURI;
    @Value("${mts_exolve.sender_number}")
    private String senderNumber;
}
