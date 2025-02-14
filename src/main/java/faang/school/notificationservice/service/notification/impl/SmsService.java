package faang.school.notificationservice.service.notification.impl;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SmsService implements NotificationService {
    private final VonageClient vonageClient;

    @Override
    public void send(UserDto user, String message) {
        TextMessage smsMessage = new TextMessage("Vonage APIs",
                user.getPhone(),
                message
        );

        SmsSubmissionResponse response = vonageClient.getSmsClient()
                .submitMessage(smsMessage);

        SmsSubmissionResponseMessage responseMessage = response.getMessages().get(0);

        if (responseMessage.getStatus() != MessageStatus.OK) {
            log.error("Message failed with error: {}", responseMessage.getErrorText());
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.SMS;
    }
}
