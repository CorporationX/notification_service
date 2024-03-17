package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.PreferredContact;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SmsService implements NotificationService {
    private final VonageClient vonageClient;

    @Override
    public void send(UserDto user, String message) {
        TextMessage textMessage = new TextMessage("CorporationX", user.getPhone(), message);

        SmsSubmissionResponse response = vonageClient.getSmsClient().submitMessage(textMessage);
        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
            log.info("Message sent " + user.getUsername() + " successfully.");
        } else {
            log.error("Failed to send message: " + response.getMessages().get(0).getErrorText());
        }
    }

    @Override
    public PreferredContact getPreferredContact() {
        return PreferredContact.SMS;
    }
}
