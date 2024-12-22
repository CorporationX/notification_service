package faang.school.notificationservice.service.sms;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.NotificationServiceException;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SmsService implements NotificationService {

    private final VonageClient vonageClient;

    @Value("${vonage.from}")
    private String from;

    @Override
    public void send(UserDto user, String message) {
        log.debug("Sending SMS message: {} to: {}", message, user.getPhone());
        validateUserHasPhoneNumber(user);
        TextMessage sms = new TextMessage(from, user.getPhone(), message);
        SmsSubmissionResponse response = vonageClient.getSmsClient().submitMessage(sms);
        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
            log.debug("Message sent successfully. {}", sms);
        } else {
            String errorMessage = response.getMessages().get(0).getErrorText();
            log.error("SMS sending failed: {}", errorMessage);
            throw new NotificationServiceException("Message sending failed: " + errorMessage);
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.SMS;
    }

    private void validateUserHasPhoneNumber(UserDto user) {
        if (user.getPhone().isBlank()) {
            throw new NotificationServiceException("The User has no phone number!");
        }
    }
}
