package faang.school.notificationservice.service;

import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import faang.school.notificationservice.dto.UserDto;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
//@Slf4j
public class SmsService implements NotificationService {
    private com.vonage.client.VonageClient vonageClient;

    @Value("${vonage.api.key}")
    private String apiKey;
    @Value("${vonage.api-secret}")
    private String apiSecret;
    @Value("${vonage.from}")
    private String from;

    @PostConstruct
    public void initializationCreatingVonageClient() {
        vonageClient = com.vonage.client.VonageClient.builder()
                .apiKey("ec1a2ee7")
                .apiSecret("HPRDmtd9nEOsdhqh")
                .build();
    }

    @Override
    public UserDto.PreferredContact getPreferredContact() {
        return UserDto.PreferredContact.SMS;
    }

    @Override
    public void send(UserDto user, String message) {
        /*TextMessage message = new TextMessage("Vonage APIs",
                "79168822014", "A text message sent using the Vonage SMS API");*/
        TextMessage textMessage = new TextMessage(from,
                user.getPhone(), "This awesome text message sent successfully using the Vonage SMS API");
        SmsSubmissionResponse response = vonageClient.getSmsClient().submitMessage(textMessage);
        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
            System.out.println("Message sent successfully.");
        } else {
            System.out.println("Message failed with error: " + response.getMessages().get(0).getErrorText());
        }
    }
}


















