package faang.school.notificationservice.exception;

import com.vonage.client.sms.MessageStatus;
import lombok.Data;

@Data
public class SmsException extends RuntimeException {
    private MessageStatus messageStatus;

    public SmsException(String message, MessageStatus messageStatus) {
        super(message);
        this.messageStatus = messageStatus;
    }


}
