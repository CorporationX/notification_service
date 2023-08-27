package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.MessageSendingException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsService implements NotificationService {

    private VonageClient vonageClient;
    @Value("${vonage.api.key}")
    private String vonageKey;
    @Value("${vonage.api.secret}")
    private String vonageSecret;
    @Value("${vonage.api.from}")
    private String from;

    @PostConstruct
    public void initVonage() {
        vonageClient = VonageClient.builder()
                .apiKey(vonageKey)
                .apiSecret(vonageSecret)
                .build();
    }

    public void sendNotification(String message, UserDto userDto) {
        TextMessage textMessage = new TextMessage(from, userDto.getPhone(), message);
        SmsSubmissionResponse response = vonageClient.getSmsClient().submitMessage(textMessage);

        MessageStatus messageStatus = response.getMessages().get(0).getStatus();
        if (messageStatus == MessageStatus.OK) {
            log.info("Message sent successfully to user, phone: {}", userDto.getPhone());
        } else {
            log.error("Message failed, status:{}, error:{}", messageStatus, response.getMessages().get(0).getErrorText());
            throw new MessageSendingException("MessageSendingException", messageStatus);
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.PHONE;
    }
}
