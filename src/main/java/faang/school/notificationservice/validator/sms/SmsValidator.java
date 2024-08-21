package faang.school.notificationservice.validator.sms;

import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SmsValidator {
    public void  validateSmsResponse(String phone, SmsSubmissionResponse response) {
        if (response.getMessages().get(0).getStatus() != MessageStatus.OK) {
            throw new RuntimeException("Failed to send SMS: " + response.getMessages().get(0).getStatus());
        }
        else {
            log.info("Message to number {} sent successfully.", phone);
        }
    }

    public void validateSmsClient(SmsClient smsClient) {
        if (smsClient == null) {
            throw new RuntimeException("SmsClient is null!");
        }
    }

    public void validateTextMessage(TextMessage textMessage) {
        if(textMessage == null) {
            throw new RuntimeException("TextMessage is null!");
        }
    }
}
