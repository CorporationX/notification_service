package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
import com.vonage.client.VonageClientException;
import com.vonage.client.VonageResponseParseException;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.SmsSendingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsService implements NotificationService {

    private final VonageClient vonageClient;

    @Override
    @Retryable(
            value = {VonageClientException.class, VonageResponseParseException.class},
            maxAttemptsExpression = "#{${vonage.retry.max-attempts}}",
            backoff = @Backoff(
                    delayExpression = "#{${vonage.retry.delay}}",
                    multiplierExpression = "#{${vonage.retry.multiplier}}"))
    public void send(UserDto user, String message) {
        TextMessage textMessage = new TextMessage("CorporationX", user.getPhone(),
                message);

        SmsSubmissionResponse response = vonageClient.getSmsClient().submitMessage(textMessage);

        MessageStatus messageStatus = response.getMessages().get(0).getStatus();

        if (messageStatus == MessageStatus.OK) {
            log.info("""
                    SMS sent successfully
                    Phone: {}
                    Message: {}
                    """,
                    user.getPhone(),
                    message);
        } else {
            String errorText = response.getMessages().get(0).getErrorText();
            log.error("""
                    SMS failed with error
                    Phone: {}
                    Message status: {}
                    Error text: {}
                    """,
                    user.getPhone(),
                    messageStatus,
                    errorText);
            throw new SmsSendingException(errorText);
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.PHONE;
    }
}
