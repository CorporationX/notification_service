package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
import com.vonage.client.VonageClientException;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SmsService implements NotificationService {
    private final VonageClient smsVonageClient;

    @Value("${vonage.api.sms-title}")
    private String smsTitle;

    @Override
    public void send(UserDto user, String message) {
        if (user.getPhone() == null || user.getPhone().isEmpty()) {
            log.error("User phone number is null or empty");
            throw new IllegalArgumentException("User phone number cannot be null or empty");
        }

        TextMessage textMessage = new TextMessage(smsTitle, user.getPhone(), message);

        try {
            SmsSubmissionResponse response = smsVonageClient
                    .getSmsClient()
                    .submitMessage(textMessage);
            if (!response.getMessages().isEmpty()
                    && response.getMessages().get(0).getStatus() == MessageStatus.OK) {
                log.info("Message sent successfully to {}", user.getPhone());
            } else {
                String error = response.getMessages().isEmpty()
                        ? "No messages in response"
                        : response.getMessages().get(0).getErrorText();
                log.error("Message failed with error: {}", error);
                throw new RuntimeException("Internal error: " + error);
            }
        } catch (VonageClientException e) {
            log.error("Failed to send SMS to {}: {}", user.getPhone(), e.getMessage());
            throw new RuntimeException("Failed to send SMS", e);
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.PHONE;
    }
}
