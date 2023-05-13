package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.messages.TextMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmsService {

    private final VonageClient vonageClient;

    public void sendSms(String to, String text) {
        vonageClient.getSmsClient().submitMessage(new TextMessage(
                "Vonage APIs",
                to,
                text
        ));
    }
}
