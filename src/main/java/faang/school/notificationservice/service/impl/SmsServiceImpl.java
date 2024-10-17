package faang.school.notificationservice.service.impl;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.service.SmsService;
import faang.school.notificationservice.service.config.SmsConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService {
    private final SmsConfig smsConfig;

    @Override
    public void sendSms(String to, String messageText) {
        TextMessage message = new TextMessage("Vonage APIs", to, messageText);
        VonageClient client = smsConfig.vonageClient();
        SmsSubmissionResponse response = client.getSmsClient().submitMessage(message);

        if (response.getMessages() != null && !response.getMessages().isEmpty()) {
            if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
                log.info("Message sent successfully.");
            } else {
                log.error("Message failed with error: {}", response.getMessages().get(0).getErrorText());
                throw new RuntimeException("Message failed with error: " + response.getMessages().get(0).getErrorText());
            }
        } else {
            log.error("No messages were returned in the response.");
            throw new RuntimeException("No messages were returned in the response.");
        }
    }
}