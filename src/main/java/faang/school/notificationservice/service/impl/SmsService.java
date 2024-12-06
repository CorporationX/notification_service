package faang.school.notificationservice.service.impl;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsService implements NotificationService {

    @Value("${vonage.api.from}")
    private String from;

    private final VonageClient vonageClient;

    @Override
    public void send(UserDto user, String message) {
        log.info("Sending SMS notification for userId: {}, message: {}", user.getId(), message);

        TextMessage textMessage = new TextMessage(from, user.getPhone(), message);

        try {
            SmsSubmissionResponse response = vonageClient.getSmsClient().submitMessage(textMessage);
            List<SmsSubmissionResponseMessage> messages = response.getMessages();

            if (messages.get(0).getStatus() == MessageStatus.OK) {
                log.info("Message sent successfully to phone: {}", user.getPhone());
            } else {
                log.warn("Message failed with error: {}", messages.get(0).getErrorText());
            }
        } catch (Exception e) {
            log.error("Error while sending SMS to phone: {}", user.getPhone(), e);
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.SMS;
    }
}
