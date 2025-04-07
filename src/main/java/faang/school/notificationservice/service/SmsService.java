package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.SmsIntegrationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SmsService implements NotificationService {

    private final VonageClient vonageClient;

    @Override
    public void send(UserDto user, String message) {
        validateUser(user);
        validateMessage(message);

        SmsSubmissionResponse response = sendSms(user, message);
        validateResponse(response);
    }

    private void validateUser(UserDto user) {
        if (user == null || user.getPhone() == null || user.getPhone().isBlank()) {
            log.warn("Phone number cannot be empty");
            throw new IllegalArgumentException("To send SMS, a phone number is a mandatory requirement");
        }
    }

    private void validateMessage(String message) {
        if (message == null || message.isBlank()) {
            log.warn("SMS message cannot be empty");
            throw new IllegalArgumentException("Message text is required");
        }
    }

    private SmsSubmissionResponse sendSms(UserDto user, String message) {
        String sender = "Test SMS from Anton B.";
        TextMessage textMessage = new TextMessage(sender, user.getPhone(), message);

        try {
            return vonageClient.getSmsClient().submitMessage(textMessage);
        } catch (Exception e) {
            log.error("Error sending SMS via Vonage", e);
            throw new SmsIntegrationException("Error sending SMS", e);
        }
    }

    private void validateResponse(SmsSubmissionResponse response) {
        if (response == null || response.getMessages() == null || response.getMessages().isEmpty()) {
            log.error("Error: empty or invalid response from SMS service");
            throw new SmsIntegrationException("Incorrect response from Vonage");
        }

        SmsSubmissionResponseMessage messageResponse = response.getMessages().get(0);
        if (messageResponse.getStatus() == MessageStatus.OK) {
            log.info("Message sent successfully");
        } else {
            log.error("The message ended with an error: {}", messageResponse.getErrorText());
            throw new SmsIntegrationException("SMS error: " + messageResponse.getErrorText());
        }
    }
    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.PHONE;
    }
}
