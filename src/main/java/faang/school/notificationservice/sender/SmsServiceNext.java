package faang.school.notificationservice.sender;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.exception.FailedSendException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsServiceNext implements NotificationServiceNext {
    private VonageClient vonageClient;
    @Value("${vonage.api.key}")
    private String apiKey;
    @Value("${vonage.api.secret}")
    private String apiSecret;
    @Value("${vonage.api.from}")
    private String from;
    @Value("${vonage.api.phone}")
    private String phone;

    @PostConstruct
    public void init() {
        vonageClient = VonageClient.builder()
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .build();
    }

    @Override
    public void send(UserDto user,String topic, String msg) {
        TextMessage message = new TextMessage(from, phone, msg);
        SmsSubmissionResponse response = vonageClient.getSmsClient().submitMessage(message);

        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
            log.info("Message sent successfully. {}", message);
        } else {
            String errorText = response.getMessages().get(0).getErrorText();
            log.error("Failed to send message. {}", errorText);
            throw new FailedSendException(errorText);
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.SMS;
    }
}

