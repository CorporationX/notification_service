package faang.school.notificationservice.service.sms;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.config.SmsProperties;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.SmsServiceException;
import faang.school.notificationservice.service.NotificationService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SmsService implements NotificationService {

    private final SmsProperties smsProperties;
    private VonageClient client;
    private static final String SMS_ERROR_MESSAGE = "Message failed with error: %s.";

    @PostConstruct
    private void init() {
        client = VonageClient.builder()
                .apiKey(smsProperties.getApi().getKey())
                .apiSecret(smsProperties.getApi().getSecret())
                .build();
    }

    @Override
    public void send(UserDto user, String message) {
        TextMessage messageRq = new TextMessage(smsProperties.getApi().getFromService(),
                user.getPhone(),
                message
        );

        SmsSubmissionResponse response = client.getSmsClient().submitMessage(messageRq);

        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
            log.info("Message sent successfully.");
        } else {
            String errMessage = SMS_ERROR_MESSAGE.formatted(response.getMessages().get(0).getErrorText());
            log.error(errMessage);
            throw new SmsServiceException(errMessage);
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.SMS;
    }
}
