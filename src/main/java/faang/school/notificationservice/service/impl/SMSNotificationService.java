package faang.school.notificationservice.service.impl;

import com.vonage.client.VonageClient;
import com.vonage.client.VonageClientException;
import com.vonage.client.VonageResponseParseException;
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
    private static final String FROM = "Corp X";
    private final VonageClient vonageClient;
    @Value("${notification-service.sms.enabled}")
    private boolean isSMSEnabled;

    @Override
    @Retryable(retryFor = {VonageClientException.class}, backoff = @Backoff(delay = 20000, multiplier = 2))
    public void send(UserDto user, String message) {
        if (IsSMSAvailable(user)) {
            return;
        }

        log.info("Send SMS notification to user: {}, message: {}", user, message);

        TextMessage textMessage = new TextMessage(FROM, user.getPhone(), message);
        try {
            SmsSubmissionResponse response = vonageClient.getSmsClient().submitMessage(textMessage);
            checkResponse(response);
        } catch (VonageResponseParseException e) {
            log.error("Error parse vonage response SMS", e);
            throw e;
        } catch (VonageClientException e) {
            log.error("Error while sending SMS", e);
            throw e;
        }
    }

    private boolean IsSMSAvailable(UserDto user) {
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

    private void checkResponse(SmsSubmissionResponse response) {
        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
            log.info("Message sent successfully.");
        } else {
            log.info("Message failed with error: {}", response.getMessages().get(0).getErrorText());
        }
    }
}
