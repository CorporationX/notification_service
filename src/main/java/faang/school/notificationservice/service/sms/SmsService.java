package faang.school.notificationservice.service.sms;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsService implements NotificationService {
    @Value("&{vonage.api.key}")
    private String vonageApiKey;
    @Value("&{vonage.api.secret}")
    private String vonageApiSecret;
    @Value("&{vonage.from}")
    private String from;

    VonageClient vonageClient = VonageClient.builder()
            .apiKey(vonageApiKey)
            .apiSecret(vonageApiSecret)
            .build();

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.SMS;
    }

    @Override
    public void send(UserDto user, String message) {
        TextMessage textMessage = new TextMessage(from, user.getPhone(), message);
        SmsClient response = vonageClient.getSmsClient();
        SmsSubmissionResponse submitMessage = response.submitMessage(textMessage);
        if (submitMessage.getMessages().get(0).getStatus() == MessageStatus.OK) {
            log.info("Message sent successfully {}", submitMessage.getMessages());
        } else {
            log.info("Message failed with error {}", submitMessage.getMessages().get(0).getErrorText());
        }
    }
}
