package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.config.VonageConfig;
import faang.school.notificationservice.dto.PregerredContactNotification;
import faang.school.notificationservice.dto.UserEventDto;
import faang.school.notificationservice.exception.SmsSendingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsService implements NotificationService {
    private final VonageConfig config;
    private final VonageClient vonageClient;

    @Override
    public void send(UserEventDto user, String message) {
        String phone = user.getPhone();

        TextMessage sms = new TextMessage(
                config.getFrom(),
                phone,
                message
        );

        SmsSubmissionResponse response = vonageClient.getSmsClient().submitMessage(sms);

        if (response.getMessages().get(0).getStatus() != MessageStatus.OK) {
            log.error("Failed to send message to {}", phone);
            throw new SmsSendingException("Failed to send message to " + phone);
        }

        log.info("Message sent successfully to {}", phone);
    }

    @Override
    public PregerredContactNotification getPreferredContact(UserEventDto dto) {
        return dto.getPreference();
    }
}
