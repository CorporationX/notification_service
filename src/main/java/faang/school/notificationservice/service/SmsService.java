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

import java.util.List;

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

        List<SmsSubmissionResponseMessage> messages = response.getMessages();
        if (messages == null || messages.isEmpty()) {
            throw new SmsSendingException(formSmsSendingExceptionMessage(userPhone));
        }

        for (SmsSubmissionResponseMessage msg : messages) {
            if (msg.getStatus() != MessageStatus.OK) {
                throw new SmsSendingException(formSmsSendingExceptionMessage(userPhone));
            }
            log.info("Message from {} to number {} with text {} sent successfully", sender, userPhone, message);
        }
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.SMS;
    }

    private String formSmsSendingExceptionMessage(String userPhone) {
        return String.format("Sending message to number %s failed with error", userPhone);
    }
}
