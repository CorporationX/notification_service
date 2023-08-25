package faang.school.notificationservice.service.sms;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.MessageSendingException;
import faang.school.notificationservice.service.NotificationService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsService implements NotificationService {
    private VonageClient client;
    @Value("${vonage.api.key}")
    private String apiKey;
    @Value("${vonage.api.secret}")
    private String apiSecret;
    @Value("${vonage.from}")
    private String from;

    @PostConstruct
    public void init() {
        client = VonageClient.builder()
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .build();
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.SMS;
    }

    @Override
    public void send(UserDto user, String msg) {
        TextMessage message = new TextMessage(from, user.getPhone(), msg);
        SmsSubmissionResponse response = client.getSmsClient().submitMessage(message);

        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
            log.info("Message sent successfully. {}", message);
        } else {
            String errorMsg = response.getMessages().get(0).getErrorText();
            log.error("Message sending failed: {}", errorMsg);
            throw new MessageSendingException("Message sending failed: " + errorMsg);
        }
    }
}
