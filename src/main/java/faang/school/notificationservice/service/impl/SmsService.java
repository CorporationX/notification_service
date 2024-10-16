package faang.school.notificationservice.service.impl;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.model.dto.UserDto;
import faang.school.notificationservice.model.enums.PreferredContact;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsService implements NotificationService {

    private final VonageClient vonageClient;

    @Override
    public void send(UserDto user, String text) {
        log.info("Sending SMS notification for user " + user.getUsername() + ", message: " + text);

        String userPhone = user.getPhone();
        String from = "79046194313";

        TextMessage message = new TextMessage(from, userPhone, text);

        try {
            SmsSubmissionResponse response = vonageClient.getSmsClient().submitMessage(message);

            for (SmsSubmissionResponseMessage responseMessage : response.getMessages()) {
                if (responseMessage.getStatus() == MessageStatus.OK) {
                    log.info("Success SMS notification for user " + user.getUsername());
                } else {
                    log.info("SMS notification failed for user " + user.getUsername()
                            + ", error: " + responseMessage.getErrorText());
                }
            }

        } catch (Exception e) {
            log.info("Failed to send SMS: " + e.getMessage(), e);
        }
    }

    @Override
    public PreferredContact getPreferredContact() {
        return PreferredContact.SMS;
    }
}
