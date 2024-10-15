package faang.school.notificationservice.service.sms;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class MtsExolveProperties {
    @Value("${mts_exolve.sms_uri}")
    private String smsURI;

    @Value("${mts_exolve.headers.auth_key}")
    private String authKey;

    @Value("${mts_exolve.headers.auth_prefix}")
    private String authPrefix;

    @Value("${mts_exolve.params.keys.sender_key}")
    private String senderKey;

    @Value("${mts_exolve.params.keys.receiver_key}")
    private String receiverKey;

    @Value("${mts_exolve.params.keys.message_key}")
    private String messageKey;

    @Value("${mts_exolve.params.values.api_key}")
    private String apiKey;

    @Value("${mts_exolve.params.values.sender_number}")
    private String senderNumber;
}
