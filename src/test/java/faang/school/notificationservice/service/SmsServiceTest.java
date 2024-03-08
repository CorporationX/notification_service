package faang.school.notificationservice.service;

import static org.junit.jupiter.api.Assertions.*;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SmsServiceTest {
    @InjectMocks
    private SmsService smsService;

    @Test
    public void testSmsStatus() {
        smsService.setApiKey("ea3cbdef");
        smsService.setApiSecret("t6qqwTma9etW5JwR");
        VonageClient client = VonageClient.builder()
                .apiKey("ea3cbdef")
                .apiSecret("t6qqwTma9etW5JwR")
                .build();
        TextMessage textMessage = new TextMessage("Vonage APIs",
                "1234567", "message"
        );

        SmsSubmissionResponse response = client.getSmsClient().submitMessage(textMessage);

        assertEquals(1, response.getMessages().size());
        assertEquals(MessageStatus.OK, response.getMessages().get(0).getStatus());
    }
}
