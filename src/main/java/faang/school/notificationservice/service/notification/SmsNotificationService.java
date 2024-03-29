package faang.school.notificationservice.service.notification;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.UserDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsNotificationService implements NotificationService {
    private final VonageClient vonageClient;
    @Value("${vonage.api.number}")
    private String appPhoneNumber;

    @Override
    public void send(UserDto user, String text) {
        String userPhoneNumber = user.getPhone();

        TextMessage message = new TextMessage(appPhoneNumber, userPhoneNumber, text);
        SmsSubmissionResponse response = vonageClient.getSmsClient().submitMessage(message);

        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
            log.info("Message sent successfully to number {} with msg {}.",
                    userPhoneNumber,
                    response.getMessages().get(0));
        } else {
            log.info("Message failed with error: {}", response.getMessages().get(0).getErrorText());
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.SMS;
    }
}
