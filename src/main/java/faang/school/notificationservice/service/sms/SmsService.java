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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SmsService implements NotificationService {

    private static final String SMS_ERROR_MESSAGE = "Message failed with error: %s.";
    private static final String NO_MESSAGES = "Vonage response contains no messages.";
    private static final String INVALID_PHONE_NUMBER = "Invalid phone number: %s";

    private final SmsProperties smsProperties;
    private final VonageClient client;

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
            log.error(INVALID_PHONE_NUMBER.formatted(phone));
            throw new SmsServiceException(INVALID_PHONE_NUMBER.formatted(phone));
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.SMS;
    }
}
