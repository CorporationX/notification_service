package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.config.notification.SmsProperties;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SmsService implements NotificationService {
    private final VonageClient client;
    private final SmsProperties smsProperties;


    @Override
    public void send(UserDto user, String message) {
        log.info("Sending message: {}. To user with id = {}", message, user.getId());
        TextMessage textMessage = new TextMessage(smsProperties.getBrandName(), user.getPhone(), message);

        SmsSubmissionResponse response = client.getSmsClient().submitMessage(textMessage);

        if (response.getMessages() == null) {
            log.error("There is no messages in sms submission response.");
            return;
        }

        SmsSubmissionResponseMessage responseMessage = response.getMessages().get(0);
        if (responseMessage.getStatus() == MessageStatus.OK) {
            log.info("Message sent successfully.");
        } else {
            log.error("Message failed with error: {}", responseMessage.getErrorText());
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.SMS;
    }
}
