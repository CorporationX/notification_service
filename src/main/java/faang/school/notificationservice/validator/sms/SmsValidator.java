package faang.school.notificationservice.validator.sms;

import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import faang.school.notificationservice.exception.SmsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SmsValidator {

    public void validateResponse(SmsSubmissionResponse response) {
        for (SmsSubmissionResponseMessage responseMessage : response.getMessages()) {
            if (responseMessage.getStatus() != MessageStatus.OK) {
                log.error("Message failed with error. Status: {}, Error message: {}",
                        response.getMessages().get(0).getStatus(),
                        response.getMessages().get(0).getErrorText());
                throw new SmsException("Message failed with error: " + response.getMessages().get(0).getErrorText(),
                        response.getMessages().get(0).getStatus());
            }
        }
    }
}
