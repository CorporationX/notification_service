package faang.school.notificationservice.service.sms;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.validator.sms.SmsValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsService implements NotificationService {
    private final SmsValidator smsValidator;

    @Value("${notification.brand}")
    private String brand;
    private final VonageClient vonageClient;

    @Override
    public void send(UserDto user, String message) {
        SmsClient smsClient = vonageClient.getSmsClient();
        smsValidator.validateSmsClient(smsClient);
        TextMessage textMessage = new TextMessage(brand, user.getPhone(), message);
        smsValidator.validateTextMessage(textMessage);
        SmsSubmissionResponse response = smsClient.submitMessage(textMessage);
        smsValidator.validateSmsResponse(user.getPhone(), response);
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.SMS;
    }
}
