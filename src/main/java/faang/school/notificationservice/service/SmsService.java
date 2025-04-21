package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
import com.vonage.client.VonageClientException;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.SmsIntegrationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Slf4j
@Validated
@RequiredArgsConstructor
public class SmsService implements NotificationService {

    private final VonageClient vonageClient;

    @Override
    public void send(@Valid UserDto user, @NotBlank String message) {

        SmsSubmissionResponse response = sendSms(user, message);
        validateResponse(response);
    }

    private SmsSubmissionResponse sendSms(UserDto user, String message) {
        TextMessage textMessage = new TextMessage("Test SMS from Anton B.", user.getPhone(), message);

        try {
            return vonageClient.getSmsClient().submitMessage(textMessage);
        } catch (VonageClientException e) {
            log.error("Error sending SMS via Vonage: {}", e.getMessage());
            throw new SmsIntegrationException("Error sending SMS: " + e.getMessage(), e);
        }
    }

    private void validateResponse(SmsSubmissionResponse response) {
        if (response.getMessages().isEmpty()) {
            log.error("Invalid response from SMS service");
            throw new SmsIntegrationException("Empty response from Vonage");
        }

        SmsSubmissionResponseMessage messageResponse = response.getMessages().get(0);
        if (messageResponse.getStatus() != MessageStatus.OK) {
            log.error("SMS delivery failed: {}", messageResponse.getErrorText());
            throw new SmsIntegrationException("Vonage error: " + messageResponse.getErrorText());
        }
        log.info("Message sent successfully");
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.PHONE;

    }
}
