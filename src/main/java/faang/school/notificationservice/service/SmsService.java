package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.config.sms.VonageConfig;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsService implements NotificationService{
    private final VonageConfig vonageConfig;

    @Override
    public void send(UserDto user, String message) {
        VonageClient client = VonageClient.builder()
                .apiKey(vonageConfig.getApiKey())
                .apiSecret(vonageConfig.getApiSecret())
                .build();

        TextMessage textMessage = new TextMessage("CorporationX_kelpie_stream10", user.getPhone(), message);
        SmsSubmissionResponse response = client.getSmsClient().submitMessage(textMessage);

        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
            log.info("SMS sent successfully to {}", user.getPhone());
        } else {
            log.error("Failed to send SMS: {}", response.getMessages().get(0).getErrorText());
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.PHONE;
    }
}