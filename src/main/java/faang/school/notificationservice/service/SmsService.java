package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.config.vonage.VonageProperties;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.MessageSendingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SmsService implements NotificationService {

    private final VonageClient vonageClient;
    private final VonageProperties vonageProperties;

    @Override
    public void send(UserDto user, String text) {
        TextMessage message = new TextMessage(
                vonageProperties.getFrom(),
                user.getPhone(),
                text
        );
        SmsSubmissionResponse response = vonageClient.getSmsClient().submitMessage(message);
        SmsSubmissionResponseMessage responseMessage = response.getMessages().get(0);

        if (responseMessage.getStatus() == MessageStatus.OK) {
            log.info("Phone submitted successfully");
        } else {
            log.error("Message failed with error: {}", responseMessage.getErrorText());
            throw new MessageSendingException("PHONE for user %s".formatted(user.getId()));
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.PHONE;
    }
}
