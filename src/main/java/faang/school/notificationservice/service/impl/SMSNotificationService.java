package faang.school.notificationservice.service.impl;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SMSNotificationService implements NotificationService {
    private static final String MESSAGE_FROM = "Corp X";

    private final VonageClient vonageClient;
    @Value("${notification-service.sms.enabled}")
    private boolean isSMSEnabled;

    @Override
    @Retryable(retryFor = {RuntimeException.class}, backoff = @Backoff(delay = 20000, multiplier = 2))
    public void send(UserDto user, String message) {
        if (isSMSNotAvailable(user)) {
            return;
        }

        log.info("Send SMS notification to user: {}, message: {}", user, message);

        TextMessage textMessage = new TextMessage(MESSAGE_FROM, user.getPhone(), message);
        try {
            SmsSubmissionResponse response = vonageClient.getSmsClient().submitMessage(textMessage);
            checkResponse(response, message);
        } catch (RuntimeException e) {
            log.error("Error vonage", e);
            throw e;
        }
    }

    private boolean isSMSNotAvailable(UserDto user) {
        if (!isSMSEnabled) {
            log.info("SMS notification is disabled");
            return true;
        }
        if (user.getPhone() == null || user.getPhone().isEmpty()) {
            log.info("User phone is empty");
            return true;
        }
        if (!UserDto.PreferredContact.SMS.equals(user.getPreference())) {
            log.info("User has not preferred SMS");
            return true;
        }
        return false;
    }

    private void checkResponse(SmsSubmissionResponse response, String message) {
        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
            log.info("Message: {} sent successfully.", message);
        } else {
            log.info("Message: {} failed with error: {}", message, response.getMessages().get(0).getErrorText());
        }
    }
}
