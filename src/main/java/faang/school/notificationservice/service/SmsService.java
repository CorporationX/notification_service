package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
import com.vonage.client.VonageClientException;
import com.vonage.client.VonageResponseParseException;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.SmsSendingException;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsService implements NotificationService {

    private final VonageClient vonageClient;

    @Value("${vonage.message.sender")
    private String senderName;

    @Override
    @Retryable(
            value = {VonageClientException.class, VonageResponseParseException.class},
            maxAttemptsExpression = "#{${vonage.retry.max-attempts}}",
            backoff = @Backoff(
                    delayExpression = "#{${vonage.retry.delay}}",
                    multiplierExpression = "#{${vonage.retry.multiplier}}"))
    public void send(@NonNull UserDto user, @NotBlank String message) {
        String userPhone = user.getPhone();
        TextMessage textMessage = new TextMessage(senderName, userPhone,
                message);

        log.info("Starting SMS sending for {}...", userPhone);
        SmsSubmissionResponse response = vonageClient.getSmsClient().submitMessage(textMessage);
        validateResponse(response);

        MessageStatus messageStatus = response.getMessages().get(0).getStatus();

        if (messageStatus == MessageStatus.OK) {
            log.info("""
                    SMS sent successfully
                    Phone: {}
                    Message: {}
                    """,
                    userPhone,
                    message);
        } else {
            String errorText = response.getMessages().get(0).getErrorText();
            log.error("""
                    SMS failed with error
                    Phone: {}
                    Message status: {}
                    Error text: {}
                    """,
                    userPhone,
                    messageStatus,
                    errorText);
            throw new SmsSendingException(errorText);
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.PHONE;
    }

    private void validateResponse(SmsSubmissionResponse response) {
        if (response == null) {
            log.error("SMS submission response is null");
            throw new IllegalStateException("SMS response is null");
        }

        if(response.getMessages() == null || response.getMessages().isEmpty()) {
            log.error("No SMS submission response messages");
            throw new IllegalStateException("No messages in SMS response");
        }
    }
}
