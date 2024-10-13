package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.VonageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import static com.vonage.client.sms.MessageStatus.OK;

@Slf4j
@RequiredArgsConstructor
@Service
public class SmsNotificationService implements NotificationService {
    private final VonageClient vonageClient;

    @Value("${vonage.sender}")
    private String sender;

    @Override
    @Retryable(
            retryFor = {VonageException.class},
            maxAttempts = 2,
            backoff = @Backoff(delay = 10000, multiplier = 6))
    public void send(UserDto user, String message) {
        String phone = user.getPhone();

        try {
            TextMessage sms = new TextMessage(sender, phone, message);
            SmsSubmissionResponse response = vonageClient.getSmsClient().submitMessage(sms);
            processResponse(response, phone);
        } catch (Exception e) {
            throw new VonageException(e.getMessage());
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.SMS;
    }

    private void processResponse(SmsSubmissionResponse response, String phone) {
        MessageStatus responseStatus = response.getMessages().get(0).getStatus();
        if (responseStatus == OK) {
            log.info("SMS was successfully sent to {}", phone);
        } else {
            throw new VonageException("SMS was not sent to %s because of %s", phone, responseStatus.getMessageStatus());
        }
    }
}
