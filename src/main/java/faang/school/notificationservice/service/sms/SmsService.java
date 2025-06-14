package faang.school.notificationservice.service.sms;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.config.SmsProperties;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.SmsServiceException;
import faang.school.notificationservice.service.NotificationService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SmsService implements NotificationService {

    private final SmsProperties smsProperties;
    private VonageClient client;
    private static final String SMS_ERROR_MESSAGE = "Message failed with error: %s.";
    private static final String NO_CREDENTIALS = "Vonage API credentials are not configured.";
    private static final String NO_MESSAGES = "Vonage response contains no messages.";
    private static final String INVALID_PHOE_NUMBER = "Invalid phone number: %s";

    @PostConstruct
    private void init() {
        if (smsProperties.getKey() == null || smsProperties.getSecret() == null) {
            log.error(NO_CREDENTIALS);
            throw new SmsServiceException(NO_CREDENTIALS);
        }
        client = VonageClient.builder()
                .apiKey(smsProperties.getKey())
                .apiSecret(smsProperties.getSecret())
                .build();
    }

    @Override
    public void send(UserDto user, String message) {
        validatePhoneNumber(user.getPhone());

        TextMessage messageRq = new TextMessage(smsProperties.getFromService(),
                user.getPhone(),
                message
        );

        log.debug("Sending SMS to {}: {}", user.getPhone(), message);
        SmsSubmissionResponse response;

        try {
            response = client.getSmsClient().submitMessage(messageRq);
            if (response.getMessages() == null || response.getMessages().isEmpty()) {
                log.error(NO_MESSAGES);
                throw new SmsServiceException(NO_MESSAGES);
            }
        } catch (Exception e) {
            log.error(NO_MESSAGES, e);
            throw new SmsServiceException(NO_MESSAGES, e);
        }

        SmsSubmissionResponseMessage firstMessage = response.getMessages().get(0);
        if (firstMessage.getStatus() == MessageStatus.OK) {
            log.info("Message sent successfully to {}.", user.getPhone());
        } else {
            String errMessage = SMS_ERROR_MESSAGE.formatted(firstMessage.getErrorText());
            log.error(errMessage);
            throw new SmsServiceException(errMessage);
        }
    }

    private void validatePhoneNumber(String phone) {
        if (phone == null || !phone.matches("^\\+?7\\d{10}$")) {
            log.error(INVALID_PHOE_NUMBER.formatted(phone));
            throw new SmsServiceException(INVALID_PHOE_NUMBER.formatted(phone));
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.SMS;
    }
}
