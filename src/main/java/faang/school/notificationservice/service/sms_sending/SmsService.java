package faang.school.notificationservice.service.sms_sending;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.exception.FailedSendException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsService implements NotificationService {
    private final VonageClient vonageClient;
    private final String from;
    private final String phone;

    @Autowired
    public SmsService(VonageClient vonageClient,
                      @Value("${vonage.api.from}") String from,
                      @Value("${vonage.api.phone}") String phone) {
        this.vonageClient = vonageClient;
        this.from = from;
        this.phone = phone;
    }

    @Override
    public void send(String msg) {
        TextMessage message = new TextMessage(from, phone, msg);

        try {
            SmsSubmissionResponse response = vonageClient.getSmsClient().submitMessage(message);

            if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
                log.info("Message sent successfully. {}", message);
            } else {
                handleSendFailure(response.getMessages().get(0).getErrorText(), message);
            }
        } catch (Exception e) {
            handleSendFailure(e.getMessage(), message);
        }
    }

    private void handleSendFailure(String errorText, TextMessage message) {
        log.error("Failed to send message. Recipient: {}. Error: {}", phone, errorText);
        throw new FailedSendException("Failed to send SMS to " + phone + ": " + errorText);
    }
}
