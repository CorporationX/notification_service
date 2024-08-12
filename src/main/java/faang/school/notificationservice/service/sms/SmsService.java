package faang.school.notificationservice.service.sms;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsService implements NotificationService {
    @Value("${notification.brand}")
    private String brand;
    private final VonageClient vonageClient;
    @Override
    public void send(UserDto user, String message) {
        SmsClient smsClient = vonageClient.getSmsClient();
        TextMessage textMessage = new TextMessage(brand, user.getPhone(), message);
        SmsSubmissionResponse response = smsClient.submitMessage(textMessage);

        if (response.getMessages().get(0).getStatus() != MessageStatus.OK) {
            throw new RuntimeException("Failed to send SMS: " + response.getMessages().get(0).getStatus());
        }
        else {
            log.info("Message to number {} sent successfully.", user.getPhone());
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.SMS;
    }
}
