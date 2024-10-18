package faang.school.notificationservice.service.sms;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.VonageException;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsService implements NotificationService {

    private final VonageClient vonageClient;

    @Value("${vonage.sender-name}")
    private String senderName;

    @Override
    @Retryable(retryFor = {VonageException.class}, backoff = @Backoff(delay = 5000))
    public void send(UserDto user, String message) {
        try {
            TextMessage textMessage = new TextMessage(senderName, user.getPhone(), message);
            SmsSubmissionResponse response = vonageClient.getSmsClient().submitMessage(textMessage);
            log.info("Message response: {}", response.getMessages());
        } catch (Exception e) {
            log.error(String.valueOf(e));
            throw new VonageException("Can't send sms notification", e);
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.SMS;
    }
}
