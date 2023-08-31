package faang.school.notificationservice.notification.sms;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.Message;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsService implements NotificationService {
    @Value("${vonage.api.key}")
    private String apiKey;

    @Value("${vonage.api.secret}")
    private String apiSecret;

    @Value("${organization.name}")
    private String organization;

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.SMS;
    }

    @Override
    public void send(UserDto user, String message) {
        Message sms = new TextMessage(
                organization,
                user.getPhone(),
                message
        );

        VonageClient client = getVonageClient();
        SmsSubmissionResponse response = client.getSmsClient().submitMessage(sms);
        smsResponse(response);
    }

    private void smsResponse(SmsSubmissionResponse response) {
        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
            log.info("Message sent successfully.");
        } else {
            log.info("Message failed with error: " + response.getMessages().get(0).getErrorText());
        }
    }

    private VonageClient getVonageClient() {
        return VonageClient.builder()
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .build();
    }
}
