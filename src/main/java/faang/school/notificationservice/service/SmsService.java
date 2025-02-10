package faang.school.notificationservice.service;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SmsService {

    private final VonageClient vonageClient;

    public SmsService(@Value("${vonage.api.key}") String apiKey,
                      @Value("${vonage.api.secret}") String apiSecret) {
        this.vonageClient = VonageClient.builder().apiKey(apiKey).apiSecret(apiSecret).build();
    }

    public void sendSms(String phone, String message) {
        TextMessage textMessage = new TextMessage("MyApp", phone, message);
        SmsSubmissionResponse response = vonageClient.getSmsClient().submitMessage(textMessage);

        response.getMessages().forEach(msg -> {
            if (msg.getStatus() == MessageStatus.OK) {
                log.info("SMS успешно отправлено на {}", phone);
            } else {
                log.error("Ошибка при отправке SMS: {}", msg.getErrorText());
            }
        });
    }
}