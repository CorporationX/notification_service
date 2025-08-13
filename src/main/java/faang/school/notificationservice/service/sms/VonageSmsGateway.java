package faang.school.notificationservice.service.sms;

import com.vonage.client.VonageClient;
import com.vonage.client.VonageClientException;
import com.vonage.client.VonageResponseParseException;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.exception.SmsSendException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class VonageSmsGateway implements SmsGateway {

    private final VonageClient client;

    @Retryable(
            retryFor = {VonageClientException.class, VonageResponseParseException.class},
            maxAttemptsExpression = "#{${vonage.retry.max-attempts}}",
            backoff = @Backoff(
                    delayExpression = "#{${vonage.retry.delay}}",
                    multiplierExpression = "#{${vonage.retry.multiplier}}"
            )
    )
    @Override
    public void send(String from, String recipientPhoneNumber, String text) {
        TextMessage textMessage = new TextMessage(from, recipientPhoneNumber, text);
        log.info("Sending SMS from {} to {} with text: {}", from, recipientPhoneNumber, text);

        SmsSubmissionResponse response = client.getSmsClient().submitMessage(textMessage);
        validateResponse(response);

        MessageStatus status = response.getMessages().get(0).getStatus();

        if (status != MessageStatus.OK) {
            String error = response.getMessages().get(0).getErrorText();
            log.error("Failed to send SMS: status={}, error={}", status, error);
            throw new SmsSendException("Vonage SMS failed: " + error);
        }

        log.info("SMS sent successfully to {}", recipientPhoneNumber);
    }

    private void validateResponse(SmsSubmissionResponse response) {
        if (response == null || response.getMessages() == null || response.getMessages().isEmpty()) {
            log.error("Vonage response is invalid or empty");
            throw new SmsSendException("Invalid Vonage SMS response");
        }
    }
}
