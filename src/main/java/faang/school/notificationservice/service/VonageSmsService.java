package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.config.sms.VonageProperty;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.EmptyApiResponseException;
import faang.school.notificationservice.exception.MessageSendException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VonageSmsService implements SmsService {
    private final VonageClient vonageClient;
    private final VonageProperty vonageProperty;

    @Override
    public void send(UserDto user, String message) {
        log.info("Sending SMS to phone={} text='{}'", user.getPhone(), message);
        TextMessage textMessage = new TextMessage(vonageProperty.sender(), user.getPhone(), message, true);

        SmsSubmissionResponse response = vonageClient.getSmsClient().submitMessage(textMessage);
        handleResponse(response);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.PHONE;
    }

    @Override
    public boolean isSupportRussianPhone() {
        return false;
    }

    private void handleResponse(SmsSubmissionResponse response) {
        if (response == null || response.getMessageCount() == 0) {
            throw new EmptyApiResponseException("No response or messages received from API: {}");
        }
        handleMessages(response.getMessages());
    }

    private void handleMessages(List<SmsSubmissionResponseMessage> messages) {
        messages.forEach(msg -> {
            if (msg.getStatus() != MessageStatus.OK) {
                throw new MessageSendException("SMS sending failed. Cause: {}", msg.getErrorText());
            }
            log.info("SMS id={} sent successfully to phone={}", msg.getId(), msg.getTo());
        });
    }
}
