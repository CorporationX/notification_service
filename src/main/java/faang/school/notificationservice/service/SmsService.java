package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
import com.vonage.client.VonageClientException;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsService implements NotificationService {

    private final VonageClient vonageClient;

    @Override
    public void send(UserDto user, String message) {
        String phone = user.getPhone();
        TextMessage smsMessage = new TextMessage("Alikante corp", phone, message);
        SmsSubmissionResponse response = vonageClient.getSmsClient().submitMessage(smsMessage);
        MessageStatus messageStatus = response.getMessages().get(response.getMessageCount() - 1).getStatus();

        if (messageStatus == MessageStatus.OK) {
            log.info("SMS submitted successfully\nPhone: " + phone + "\nMessage: " + message);
        }
        else {
            log.error("SMS submitted failed\nPhone: " + phone + "\nMessage: " + message + "\nMessageStatus:" + messageStatus);
            throw new VonageClientException("SMS submitted failed");
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.SMS;
    }
}
