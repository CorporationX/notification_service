package faang.school.notificationservice.validator.sms;

import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import faang.school.notificationservice.exception.DataValidationException;
import faang.school.notificationservice.exception.SmsException;
import org.springframework.stereotype.Component;

@Component
public class SmsValidator {

    public void validateMessage(String message) {
        if (message.isBlank() || message.isEmpty()) {
            throw new DataValidationException("The message is missing");
        }
    }

    public void validateResponse(SmsSubmissionResponse response) {
        for (SmsSubmissionResponseMessage responseMessage : response.getMessages()) {
            if (responseMessage.getStatus() != MessageStatus.OK) {
                throw new SmsException("Message failed with error: " + response.getMessages().get(0).getErrorText(),
                        response.getMessages().get(0).getStatus());
            }
        }
    }
}
