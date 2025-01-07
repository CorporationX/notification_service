package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsService implements NotificationService {

    private final VonageClient client;

    @Value("${company.name}")
    private String companyName;


    @Override
    public void send(UserDto user, String message) {
        TextMessage TextMessage = new TextMessage(companyName,
                user.getPhone(), message
        );

        SmsSubmissionResponse response = client.getSmsClient().submitMessage(TextMessage);

        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
            log.info("Message sent successfully for user with number {}", user.getPhone());
        } else {
            log.error("Message failed with error: {}", response.getMessages().get(0).getErrorText());
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.SMS;
    }
}
