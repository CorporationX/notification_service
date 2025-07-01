package faang.school.notificationservice.service;

import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.config.sms.SmsVonageClient;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsService implements NotificationService {
    @Value("${vonage.sender}")
    private final String sender;

    private final SmsVonageClient smsClient;

    @Override
    public void send(UserDto user, String message) {

        TextMessage textMessage = new TextMessage(sender, user.getPhone(), message);
        SmsSubmissionResponse response = smsClient.getClient().getSmsClient().submitMessage(textMessage);

        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
            log.info("SMS sent successfully to {}", user.getPhone());
        } else {
            log.error("Failed to send SMS: {}", response.getMessages().get(0).getErrorText());
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.PHONE;
    }
}