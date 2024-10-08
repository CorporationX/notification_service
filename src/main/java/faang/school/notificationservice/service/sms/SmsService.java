package faang.school.notificationservice.service.sms;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.config.sms.SmsConfig;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsService implements NotificationService {

    private final VonageClient vonageClient;
    private final SmsConfig smsConfig;

    @Override
    public void send(UserDto user, String message) {
        log.info("Sending message: {} to userId = {}", message, user.getId());

        TextMessage textMessage = createTextMessage(user, message);
        SmsSubmissionResponse response = vonageClient.getSmsClient().submitMessage(textMessage);

        handleSmsResponse(response);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.SMS;
    }

    private TextMessage createTextMessage(UserDto user, String message) {
        return new TextMessage(
                smsConfig.getSender(),
                user.getPhone(),
                message
        );
    }

    private void handleSmsResponse(SmsSubmissionResponse response) {
        if (response == null) {
            log.error("SMS submission response is null.");
            return;
        }

        if (response.getMessages() == null || response.getMessages().isEmpty()) {
            log.error("No messages found in SMS submission response.");
            return;
        }

        response.getMessages().forEach(message -> {
            if (message.getStatus() == MessageStatus.OK) {
                log.info("Message sent successfully to {}.", message.getTo());
            } else {
                log.error("Message failed with error: {} (Code: {})", message.getErrorText(), message.getStatus().name());
            }
        });
    }
}
