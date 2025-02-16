package faang.school.notificationservice.service.impl;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.config.sms.SmsServiceProperties;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsService implements NotificationService {

    private final VonageClient client;
    private final SmsServiceProperties properties;

    @Override
    public void send(UserDto user, String message) {
        TextMessage textMessage = new TextMessage(properties.getFrom(), user.getPhone(), message);

        SmsSubmissionResponse response = client.getSmsClient().submitMessage(textMessage);
        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
            log.info("Message sent successfully.");
        } else {
            log.error("Message failed with error: " + response.getMessages().get(0).getErrorText());
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.SMS;
    }
}
