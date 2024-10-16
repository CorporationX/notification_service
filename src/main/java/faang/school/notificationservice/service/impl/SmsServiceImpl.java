package faang.school.notificationservice.service.impl;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.service.SmsService;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService {

    private final VonageClient client;

    public SmsServiceImpl() {
        this.client = VonageClient.builder()
                .apiKey("ea3cbdef")
                .apiSecret("t6qqwTma9etW5JwR")
                .build();
    }

    @Override
    public void sendSms(String to, String messageText) {
        TextMessage message = new TextMessage("Vonage APIs", to, messageText);
        SmsSubmissionResponse response = client.getSmsClient().submitMessage(message);

        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
            System.out.println("Message sent successfully.");
        } else {
            System.out.println("Message failed with error: " + response.getMessages().get(0).getErrorText());
        }
    }
}