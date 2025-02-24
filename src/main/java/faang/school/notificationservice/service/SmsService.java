package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.config.vanage.VonageProperties;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.SmsSendingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsService implements NotificationService {

    private final VonageClient vonageClient;

    private final VonageProperties vonageProperties;

    @Override
    public void send(UserDto user, String message) {
        String sender = vonageProperties.getFrom();
        String userPhone = user.getPhone();

        TextMessage textMessage = new TextMessage(sender, userPhone, message);

        SmsSubmissionResponse response = vonageClient.getSmsClient().submitMessage(textMessage);
        SmsSubmissionResponseMessage firstResponseMessage = response.getMessages().get(0);

        if (firstResponseMessage.getStatus() == MessageStatus.OK) {
            log.info("Message from {} to number {} with text {} sent successfully", sender, userPhone, message);
        } else {
            log.error("Sending message to number {} failed with error: {}",
                    userPhone, firstResponseMessage.getErrorText());
            throw new SmsSendingException("Sending message to number " + userPhone + " failed with error");
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.SMS;
    }
}
