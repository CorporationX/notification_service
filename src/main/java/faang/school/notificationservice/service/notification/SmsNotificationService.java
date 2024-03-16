package faang.school.notificationservice.service.notification;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsNotificationService implements NotificationService {

    @Override
    public void send(UserDto user, String message) {
        System.out.println(message);
    }
    /*@Value("${vonage.api.key}")
    private String apiKey;
    @Value("${vonage.api.secret}")
    private String apiSecret;
    @Value("${vonage.api.number}")
    private String appPhoneNumber;

    @Override
    public void send(UserDto user, String message) {
        VonageClient client = VonageClient.builder()
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .build();

        String userPhoneNumber = user.getPhone();

        TextMessage msg = new TextMessage(
                appPhoneNumber,
                userPhoneNumber,
                message
        );

        SmsSubmissionResponse response = client.getSmsClient().submitMessage(msg);

        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
            log.info("Message sent successfully to number {} with msg {}.", userPhoneNumber, message);
        } else {
            log.warn("Message failed with error: " + response.getMessages().get(0).getErrorText());
        }
    }*/

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.SMS;
    }
}