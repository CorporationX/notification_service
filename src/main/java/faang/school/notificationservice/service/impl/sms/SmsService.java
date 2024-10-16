package faang.school.notificationservice.service.impl.sms;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.validator.sms.SmsValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsService implements NotificationService {
    private final VonageClient vonageClient;
    private final SmsValidator smsValidator;
    @Value("${vonage.api.band}")
    private String bandName;

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.SMS;
    }

    @Override
    public void send(UserDto user, String message) {
        TextMessage textMessage = new TextMessage(bandName, user.getPhone(), message);

        SmsSubmissionResponse response = vonageClient.getSmsClient()
                .submitMessage(textMessage);
        smsValidator.validateResponse(response);
        log.info("Message sent successfully. response = {}", response.getMessages());
    }
}
