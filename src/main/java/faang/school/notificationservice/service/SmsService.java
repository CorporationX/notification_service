package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    VonageClient client;

    public SmsService() {
        client = VonageClient.builder().apiKey("54dc6c7b").apiSecret("Ib1jPHqLrt39YSXJ").build();
    }
    public void send() {
        TextMessage message = new TextMessage("Vonage APIs",
                "7074787636",
                "A text message sent using the Vonage SMS API"
        );

        SmsSubmissionResponse response = client.getSmsClient().submitMessage(message);

        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
            System.out.println("Message sent successfully.");
        } else {
            System.out.println("Message failed with error: " + response.getMessages().get(0).getErrorText());
        }
    }
}
