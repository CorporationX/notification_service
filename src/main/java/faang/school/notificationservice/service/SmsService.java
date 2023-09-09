package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Value;

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
    public void send(UserDto userDto, String text) {
        TextMessage message = new TextMessage(from, userDto.getPhone(), text);
        SmsSubmissionResponse response = client.getSmsClient().submitMessage(message);
        log.info("Sending message: {}, to phone number: {}", text, userDto.getPhone());

        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
            log.info("Message sent successfully. {}", message.getMessageBody());
        } else {
            String errorMsg = response.getMessages().get(0).getErrorText();
            log.error("Message sending failed: {}", errorMsg);
        }
    }
}