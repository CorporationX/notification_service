package faang.school.notificationservice.service.sms;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class SmsNotificationProperties {
    @Value("${sms_service.mts_exolve_api.sms_uri}")
    private String smsURI;

    @Value("${sms_service.mts_exolve_api.headers.auth_key}")
    private String authKey;

    @Value("${sms_service.mts_exolve_api.headers.auth_prefix}")
    private String authPrefix;

    @Value("${sms_service.mts_exolve_api.params.keys.sender_key}")
    private String senderKey;

    @Value("${sms_service.mts_exolve_api.params.keys.receiver_key}")
    private String receiverKey;

    @Value("${sms_service.mts_exolve_api.params.keys.message_key}")
    private String messageKey;

    @Value("${sms_service.mts_exolve_api.params.values.api_key}")
    private String apiKey;

    @Value("${sms_service.mts_exolve_api.params.values.sender_number}")
    private String senderNumber;

    @Value("${sms_service.retryable.max_attempts}")
    private String maxAttempts;

    @Value("${sms_service.retryable.delay}")
    private String delay;
}
