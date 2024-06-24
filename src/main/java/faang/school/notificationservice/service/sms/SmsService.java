package faang.school.notificationservice.service.sms;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.entity.PreferredContact;
import faang.school.notificationservice.exception.SmsSendingException;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SmsService implements NotificationService {

    private final VonageClient vonageClient;

    @Value("${vonage.from}")
    private String from;

    @Override
    public PreferredContact getPreferredContact() {
        return PreferredContact.SMS;
    }

    @Override
    public void send(UserDto user, String msg) {
        TextMessage message = new TextMessage(from, user.getPhone(), msg);
        SmsSubmissionResponse response = vonageClient.getSmsClient().submitMessage(message);

        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
            log.info("Message sent successfully. {}", message);
        } else {
            String errorMsg = response.getMessages().get(0).getErrorText();
            throw new SmsSendingException("Message sending failed: " + errorMsg);
        }
    }
}
