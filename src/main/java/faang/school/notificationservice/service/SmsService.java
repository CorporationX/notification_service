package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
import com.vonage.client.VonageClientException;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.handler.SmsSendingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class SmsService implements NotificationService {
    private final VonageClient smsVonageClient;

    @Value("${vonage.api.sms-title}")
    private String smsTitle;

    @Async("smsTaskExecutor")
    @Override
    public CompletableFuture<Void> send(UserDto user, String message) {
        CompletableFuture<Void> future;
        try {
            validateNumber(user.getPhone());

            TextMessage textMessage = new TextMessage(smsTitle, user.getPhone(), message);

            SmsSubmissionResponse response = smsVonageClient
                    .getSmsClient()
                    .submitMessage(textMessage);
            if (!response.getMessages().isEmpty()
                    && response.getMessages().get(0).getStatus() == MessageStatus.OK) {
                log.info("Message sent successfully to {}", user.getPhone());
                future = CompletableFuture.completedFuture(null);
            } else {
                String error = response.getMessages().isEmpty()
                        ? "No messages in response"
                        : response.getMessages().get(0).getErrorText();
                log.error("Message failed with error: {}", error);
                future =  CompletableFuture.failedFuture(new SmsSendingException("Internal error: " + error));
            }
        } catch (VonageClientException e) {
            log.error("Failed to send SMS to {}: {}", user.getPhone(), e.getMessage());
            future = CompletableFuture.failedFuture(new SmsSendingException("Failed to send SMS", e));
        } catch (SmsSendingException e) {
            log.error("SMS sending failed for user {}: {}", user.getPhone(), e.getMessage());
            future = CompletableFuture.failedFuture(e);
        }

        return future.whenComplete((result, throwable) -> {
            if (throwable != null) {
                log.error("SMS sending to {} failed: {}", user.getPhone(), throwable.getMessage());
            } else {
                log.info("SMS sending to {} completed successfully", user.getPhone());
            }
        });
    }

    private boolean validateNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            log.error("User phone number is null or empty");
            throw new SmsSendingException("User phone number cannot be null or empty");
        }
        String regex = "^\\+7\\s?(\\(\\d{3}\\)\\s?|\\d{3}\\s?)\\d{3}-?\\d{2}-?\\d{2}$";
        boolean isValid = phoneNumber.matches(regex);
        if (!isValid) {
            log.error("User phone number has incorrect format");
            throw new SmsSendingException("User phone number has incorrect format: " + phoneNumber);
        }
        return true;
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.PHONE;
    }
}
